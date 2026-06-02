package com.quizquest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Verifies the server — not the browser — decides whether a timed quiz expired, using a clock we
 * can fast-forward.
 */
@SpringBootTest
@AutoConfigureMockMvc
class QuizTimingTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    @Autowired
    Clock clock;

    @Test
    void submittingAfterTheDeadlineMarksTheQuizExpired() throws Exception {
        MutableClock mutable = (MutableClock) clock;
        mutable.setNow(Instant.now());

        // 10-second timed quiz
        String session = mvc.perform(post("/api/quizzes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of(
                                "studentName", "Late", "classLevel", 9, "subject", "Mathematics",
                                "count", 2, "mode", "TIMED", "durationSeconds", 10))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        long sessionId = json.readTree(session).get("sessionId").asLong();

        // jump well past the deadline, then submit
        mutable.setNow(Instant.now().plusSeconds(200));
        ObjectNode submit = json.createObjectNode();
        submit.set("answers", json.createArrayNode());

        String result = mvc.perform(post("/api/quizzes/" + sessionId + "/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(submit)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode body = json.readTree(result);
        assertThat(body.get("status").asText()).isEqualTo("EXPIRED");
    }

    @TestConfiguration
    static class FixedClockConfig {
        @Bean
        @Primary
        Clock testClock() {
            return new MutableClock(Instant.now());
        }
    }

    /** A clock whose "now" can be moved forward by a test. */
    static class MutableClock extends Clock {
        private volatile Instant now;

        MutableClock(Instant now) {
            this.now = now;
        }

        void setNow(Instant now) {
            this.now = now;
        }

        @Override
        public Instant instant() {
            return now;
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }
    }
}
