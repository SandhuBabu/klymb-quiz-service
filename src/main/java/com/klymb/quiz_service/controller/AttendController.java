package com.klymb.quiz_service.controller;

import com.klymb.quiz_service.dto.QuizForParticipantDto;
import com.klymb.quiz_service.service.AttendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attend")
@RequiredArgsConstructor
public class AttendController {

    private final AttendService attendService;

    @GetMapping("/quiz/{id}")
    public QuizForParticipantDto getQuizForParticipant(@PathVariable String id) {
        return attendService.getQuizForParticipant(id);
    }
}
