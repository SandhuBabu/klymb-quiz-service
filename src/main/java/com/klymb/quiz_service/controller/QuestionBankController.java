package com.klymb.quiz_service.controller;

import com.klymb.quiz_service.dto.QuestionBankFormDto;
import com.klymb.quiz_service.entity.Question;
import com.klymb.quiz_service.entity.QuestionBank;
import com.klymb.quiz_service.entity.TenantKeyValue;
import com.klymb.quiz_service.service.QuestionBankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;


@RestController
@RequestMapping("/api/question-bank")
@RequiredArgsConstructor
public class QuestionBankController {

    private final QuestionBankService questionBankService;

    @PostMapping
    public ResponseEntity<QuestionBank> createQuestionBank(@Valid @ModelAttribute QuestionBankFormDto dto) throws IOException {
        var response = questionBankService.createQuestion(dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/category")
    public Set<TenantKeyValue> getQuestionBankCategories() {
        return questionBankService.getQuestionBankCategories();
    }

    @GetMapping("/{id}")
    public QuestionBank getQuestionBank(@PathVariable String id) {
        return questionBankService.getQuestionBankById(id);
    }
}
