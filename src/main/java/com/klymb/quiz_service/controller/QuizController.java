package com.klymb.quiz_service.controller;

import com.klymb.quiz_service.dto.QuizFormDto;
import com.klymb.quiz_service.dto.QuizReScheduleFormDto;
import com.klymb.quiz_service.dto.QuizSummary;
import com.klymb.quiz_service.entity.Quiz;
import com.klymb.quiz_service.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestBody QuizFormDto dto) {
        var response = quizService.createQuiz(dto);
        return ResponseEntity.status(201).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping
    public List<QuizSummary> getAllQuiz() {
        return quizService.getAllQuiz();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping("/{id}")
    public Quiz getQuizDetail(@PathVariable String id) {
        return quizService.getQuizDetail(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PatchMapping("/{id}/cancel")
    public void cancelQuiz(@PathVariable String id) {
        quizService.cancelQuiz(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PatchMapping("/{id}/re-schedule")
    public Quiz reSchedule(@PathVariable String id, @Valid @RequestBody QuizReScheduleFormDto dto) {
        return quizService.reSchedule(id, dto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable String id) {
        quizService.deleteQuiz(id);
    }

}
