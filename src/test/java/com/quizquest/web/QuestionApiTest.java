package com.quizquest.web;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionApiTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    private String body(Object value) throws Exception {
        return json.writeValueAsString(value);
    }

    @Test
    void createUpdateAndDeleteQuestion() throws Exception {
        Map<String, Object> input = Map.of(
                "classLevel", 6, "subject", "TestSubject", "difficulty", "EASY",
                "text", "What is 2 + 2?", "options", java.util.List.of("3", "4", "5", "6"),
                "correctIndex", 1, "explanation", "2 + 2 = 4");

        String created = mvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON).content(body(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.points", is(1)))
                .andReturn().getResponse().getContentAsString();
        long id = json.readTree(created).get("id").asLong();

        // update it
        Map<String, Object> updated = new java.util.HashMap<>(input);
        updated.put("text", "What is 3 + 3?");
        updated.put("correctIndex", 3);
        updated.put("options", java.util.List.of("4", "5", "6", "6"));
        mvc.perform(put("/api/questions/" + id)
                        .contentType(MediaType.APPLICATION_JSON).content(body(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("What is 3 + 3?")));

        // delete it
        mvc.perform(delete("/api/questions/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/questions/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void correctIndexOutOfRangeIsRejected() throws Exception {
        Map<String, Object> input = Map.of(
                "classLevel", 6, "subject", "TestSubject", "difficulty", "EASY",
                "text", "Bad question", "options", java.util.List.of("a", "b"),
                "correctIndex", 5, "explanation", "");
        mvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON).content(body(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void blankTextFailsValidation() throws Exception {
        Map<String, Object> input = Map.of(
                "classLevel", 6, "subject", "TestSubject", "difficulty", "EASY",
                "text", "", "options", java.util.List.of("a", "b"),
                "correctIndex", 0, "explanation", "");
        mvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON).content(body(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.text").exists());
    }

    @Test
    void seededBankIsBrowsable() throws Exception {
        String list = mvc.perform(get("/api/questions").param("classLevel", "6")
                        .param("subject", "Mathematics"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode arr = json.readTree(list);
        org.assertj.core.api.Assertions.assertThat(arr.isArray()).isTrue();
        org.assertj.core.api.Assertions.assertThat(arr.size()).isGreaterThanOrEqualTo(3);
    }
}
