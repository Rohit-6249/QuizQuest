package com.quizquest.web;

import com.quizquest.dto.LeaderboardEntryDto;
import com.quizquest.dto.QuizSessionDto;
import com.quizquest.dto.ResultDto;
import com.quizquest.dto.StartQuizRequest;
import com.quizquest.dto.SubmitRequest;
import com.quizquest.service.QuizService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Running a quiz: start, resync the live session, submit for grading, and read the result. Also
 * serves the leaderboard.
 */
@RestController
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/api/quizzes")
    public QuizSessionDto start(@Valid @RequestBody StartQuizRequest request) {
        return quizService.start(request);
    }

    @GetMapping("/api/quizzes/{id}")
    public QuizSessionDto live(@PathVariable Long id) {
        return quizService.live(id);
    }

    @PostMapping("/api/quizzes/{id}/submit")
    public ResultDto submit(@PathVariable Long id, @Valid @RequestBody SubmitRequest request) {
        return quizService.submit(id, request);
    }

    @GetMapping("/api/quizzes/{id}/result")
    public ResultDto result(@PathVariable Long id) {
        return quizService.result(id);
    }

    @GetMapping("/api/leaderboard")
    public List<LeaderboardEntryDto> leaderboard(
            @RequestParam(required = false) Integer classLevel,
            @RequestParam(required = false) String subject,
            @RequestParam(defaultValue = "10") int limit) {
        return quizService.leaderboard(classLevel, subject, Math.max(1, Math.min(50, limit)));
    }
}
