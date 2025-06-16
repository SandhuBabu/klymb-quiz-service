package com.klymb.quiz_service.controller;

import com.klymb.quiz_service.dto.QuizForParticipantDto;
import com.klymb.quiz_service.service.AttemptService;
import com.klymb.quiz_service.service.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attempt")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    @GetMapping("/quiz/{id}")
    public ResponseEntity<QuizForParticipantDto> getQuizForParticipant(@PathVariable String id) {
        var res = attemptService.getQuizForParticipant(id);
        return ResponseEntity.status(201).body(res);
    }
}
