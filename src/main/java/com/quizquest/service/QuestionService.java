package com.quizquest.service;

import com.quizquest.domain.Difficulty;
import com.quizquest.domain.Question;
import com.quizquest.dto.QuestionDto;
import com.quizquest.dto.QuestionInput;
import com.quizquest.repository.QuestionRepository;
import com.quizquest.web.BadRequestException;
import com.quizquest.web.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manages the question bank: browsing, and the admin create / update / delete operations.
 */
@Service
public class QuestionService {

    private final QuestionRepository questions;

    public QuestionService(QuestionRepository questions) {
        this.questions = questions;
    }

    @Transactional(readOnly = true)
    public List<QuestionDto> search(Integer classLevel, String subject, Difficulty difficulty) {
        return questions.search(classLevel, blankToNull(subject), difficulty).stream()
                .map(QuestionDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionDto get(Long id) {
        return QuestionDto.from(find(id));
    }

    @Transactional
    public QuestionDto create(QuestionInput input) {
        validate(input);
        Question q = new Question(
                input.classLevel(), input.subject().trim(), input.difficulty(),
                input.text().trim(), input.options(), input.correctIndex(),
                trimOrNull(input.explanation()));
        return QuestionDto.from(questions.save(q));
    }

    @Transactional
    public QuestionDto update(Long id, QuestionInput input) {
        validate(input);
        Question q = find(id);
        q.setClassLevel(input.classLevel());
        q.setSubject(input.subject().trim());
        q.setDifficulty(input.difficulty());
        q.setText(input.text().trim());
        q.setOptions(input.options());
        q.setCorrectIndex(input.correctIndex());
        q.setExplanation(trimOrNull(input.explanation()));
        return QuestionDto.from(questions.save(q));
    }

    @Transactional
    public void delete(Long id) {
        if (!questions.existsById(id)) {
            throw new NotFoundException("Question " + id + " not found");
        }
        questions.deleteById(id);
    }

    private Question find(Long id) {
        return questions.findById(id)
                .orElseThrow(() -> new NotFoundException("Question " + id + " not found"));
    }

    private void validate(QuestionInput input) {
        if (input.correctIndex() >= input.options().size()) {
            throw new BadRequestException(
                    "correctIndex " + input.correctIndex() + " is out of range for "
                            + input.options().size() + " options");
        }
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private static String trimOrNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
