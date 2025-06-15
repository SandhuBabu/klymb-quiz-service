package com.klymb.quiz_service.controller;

import com.klymb.quiz_service.dto.QuestionBankFormDto;
import com.klymb.quiz_service.entity.Question;
import com.klymb.quiz_service.entity.QuestionBank;
import com.klymb.quiz_service.entity.TenantKeyValue;
import com.klymb.quiz_service.entity.enums.QuestionBankStatus;
import com.klymb.quiz_service.dto.QuestionBankSummary;
import com.klymb.quiz_service.service.QuestionBankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/api/question-bank")
@RequiredArgsConstructor
public class QuestionBankController {

    private final QuestionBankService questionBankService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<QuestionBank> createQuestionBank(@Valid @ModelAttribute QuestionBankFormDto dto) throws IOException {
        var response = questionBankService.createQuestion(dto);
        return ResponseEntity.status(201).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping("/category")
    public Set<TenantKeyValue> getQuestionBankCategories() {
        return questionBankService.getQuestionBankCategories();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping("/{id}")
    public QuestionBank getQuestionBank(@PathVariable String id) {
        return questionBankService.getQuestionBankById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping
    public List<QuestionBankSummary> getQuestionsBanks(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) QuestionBankStatus status
    ) {
        return questionBankService.getAllQuestionBank(title, status, category);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    public void deleteQuestionBank(@PathVariable String id) {
        questionBankService.deleteQuestionBank(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PatchMapping("/{id}/status")
    public void updateQuestionBankStatus(
            @RequestBody Map<String, QuestionBankStatus> questionBankStatus,
            @PathVariable String id
    ) {
        questionBankService.updateQuestionBankStatus(questionBankStatus, id);
    }
}
