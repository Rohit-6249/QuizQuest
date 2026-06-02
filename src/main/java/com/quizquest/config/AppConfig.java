package com.quizquest.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Shared beans. The {@link Clock} is injected wherever "now" is needed so that time-based logic
 * (quiz deadlines, elapsed time) can be tested with a fixed clock.
 */
@Configuration
public class AppConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
