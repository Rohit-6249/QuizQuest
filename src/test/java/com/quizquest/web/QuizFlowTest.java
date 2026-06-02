package com.quizquest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class QuizFlowTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    private JsonNode postJson(String url, Object body) throws Exception {
        String response = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return json.readTree(response);
    }

    @Test
    void answeringEveryQuestionCorrectlyGivesFullScore() throws Exception {
        Map<String, Object> start = Map.of(
                "studentName", "Ravi", "classLevel", 6, "subject", "Mathematics",
                "count", 5, "mode", "PRACTICE");
        JsonNode session = postJson("/api/quizzes", start);
        long sessionId = session.get("sessionId").asLong();
        JsonNode questions = session.get("questions");
        assertThat(questions.size()).isGreaterThan(0);
        // the live quiz must never leak the answer
        assertThat(questions.get(0).has("correctIndex")).isFalse();

        // look up each correct answer through the admin endpoint, then answer correctly
        ArrayNode answers = json.createArrayNode();
        for (JsonNode q : questions) {
            long qid = q.get("questionId").asLong();
            String full = mvc.perform(get("/api/questions/" + qid))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
            int correctIndex = json.readTree(full).get("correctIndex").asInt();
            ObjectNode answer = json.createObjectNode();
            answer.put("questionId", qid);
            answer.put("selectedIndex", correctIndex);
            answers.add(answer);
        }

        ObjectNode submit = json.createObjectNode();
        submit.set("answers", answers);
        JsonNode result = postJson("/api/quizzes/" + sessionId + "/submit", submit);

        assertThat(result.get("status").asText()).isEqualTo("SUBMITTED");
        assertThat(result.get("correctCount").asInt()).isEqualTo(questions.size());
        assertThat(result.get("score").asInt()).isEqualTo(result.get("maxScore").asInt());
        assertThat(result.get("accuracy").asDouble()).isEqualTo(1.0);

        // the student should now appear on the leaderboard
        String board = mvc.perform(get("/api/leaderboard"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(board).contains("Ravi");
    }

    @Test
    void wrongAnswersAreGradedAndAnswersRevealedOnlyAfterSubmit() throws Exception {
        Map<String, Object> start = Map.of(
                "studentName", "Meera", "classLevel", 7, "subject", "Science",
                "count", 3, "mode", "PRACTICE");
        JsonNode session = postJson("/api/quizzes", start);
        long sessionId = session.get("sessionId").asLong();

        // submit nothing -> everything wrong, but review now reveals correct answers + explanations
        ObjectNode submit = json.createObjectNode();
        submit.set("answers", json.createArrayNode());
        JsonNode result = postJson("/api/quizzes/" + sessionId + "/submit", submit);

        assertThat(result.get("correctCount").asInt()).isZero();
        assertThat(result.get("score").asInt()).isZero();
        JsonNode review = result.get("review");
        assertThat(review.size()).isEqualTo(session.get("questions").size());
        assertThat(review.get(0).has("correctIndex")).isTrue();
    }

    @Test
    void resubmittingReturnsTheSameResult() throws Exception {
        JsonNode session = postJson("/api/quizzes", Map.of(
                "studentName", "Sam", "classLevel", 8, "subject", "English",
                "count", 2, "mode", "PRACTICE"));
        long sessionId = session.get("sessionId").asLong();
        ObjectNode submit = json.createObjectNode();
        submit.set("answers", json.createArrayNode());

        JsonNode first = postJson("/api/quizzes/" + sessionId + "/submit", submit);
        JsonNode second = postJson("/api/quizzes/" + sessionId + "/submit", submit);
        assertThat(second.get("sessionId").asLong()).isEqualTo(first.get("sessionId").asLong());
        assertThat(second.get("score").asInt()).isEqualTo(first.get("score").asInt());
    }

    @Test
    void impossibleComboIsRejected() throws Exception {
        mvc.perform(post("/api/quizzes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of(
                                "studentName", "Nobody", "classLevel", 6,
                                "subject", "Astrophysics", "count", 5, "mode", "PRACTICE"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void aHardSingleSubjectQuizHasNoRepeatedQuestions() throws Exception {
        JsonNode session = postJson("/api/quizzes", Map.of(
                "studentName", "Distinct", "classLevel", 10, "subject", "Mathematics",
                "difficulty", "HARD", "count", 20, "mode", "PRACTICE"));
        JsonNode questions = session.get("questions");
        assertThat(questions.size()).isEqualTo(20);
        java.util.Set<Long> ids = new java.util.HashSet<>();
        for (JsonNode q : questions) {
            ids.add(q.get("questionId").asLong());
        }
        assertThat(ids).hasSize(20); // all distinct
    }

    @Test
    void aMixedQuizIsBalancedAcrossSubjects() throws Exception {
        JsonNode session = postJson("/api/quizzes", Map.of(
                "studentName", "Mixer", "classLevel", 8, "count", 12, "mode", "PRACTICE"));
        JsonNode questions = session.get("questions");
        assertThat(questions.size()).isEqualTo(12);

        java.util.Map<String, Integer> bySubject = new java.util.HashMap<>();
        java.util.Set<Long> ids = new java.util.HashSet<>();
        for (JsonNode q : questions) {
            bySubject.merge(q.get("subject").asText(), 1, Integer::sum);
            ids.add(q.get("questionId").asLong());
        }
        assertThat(ids).hasSize(12); // no repeats
        assertThat(bySubject).hasSize(4); // all four subjects present
        int max = bySubject.values().stream().max(Integer::compare).orElse(0);
        int min = bySubject.values().stream().min(Integer::compare).orElse(0);
        assertThat(max - min).isLessThanOrEqualTo(1); // evenly distributed (3 each)
    }
}
