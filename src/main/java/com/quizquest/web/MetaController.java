package com.quizquest.web;

import com.quizquest.dto.MetaDto;
import com.quizquest.dto.StatsDto;
import com.quizquest.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lookup data for the forms, and the home-dashboard stats.
 */
@RestController
@RequestMapping("/api")
public class MetaController {

    private final StatsService statsService;

    public MetaController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/meta")
    public MetaDto meta() {
        return statsService.meta();
    }

    @GetMapping("/stats")
    public StatsDto stats() {
        return statsService.stats();
    }
}
