package com.klymb.quiz_service.controller;

import com.klymb.quiz_service.entity.Question;
import com.klymb.quiz_service.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping("/question-bank/{id}")
    public List<Question> getAllQuestionsForQuestionBank(@PathVariable String id) {
        return questionService.getAllQuestionsForQuestionBank(id);
    }

}
