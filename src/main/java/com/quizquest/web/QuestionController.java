package com.quizquest.web;

import com.quizquest.domain.Difficulty;
import com.quizquest.dto.QuestionDto;
import com.quizquest.dto.QuestionInput;
import com.quizquest.service.QuestionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The question bank / admin API: browse with optional filters, and create / update / delete.
 */
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<QuestionDto> list(@RequestParam(required = false) Integer classLevel,
                                  @RequestParam(required = false) String subject,
                                  @RequestParam(required = false) Difficulty difficulty) {
        return questionService.search(classLevel, subject, difficulty);
    }

    @GetMapping("/{id}")
    public QuestionDto get(@PathVariable Long id) {
        return questionService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDto create(@Valid @RequestBody QuestionInput input) {
        return questionService.create(input);
    }

    @PutMapping("/{id}")
    public QuestionDto update(@PathVariable Long id, @Valid @RequestBody QuestionInput input) {
        return questionService.update(id, input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        questionService.delete(id);
    }
}
