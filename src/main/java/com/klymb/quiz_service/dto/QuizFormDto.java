package com.klymb.quiz_service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizFormDto {
    private String questionBankId;

    @Size(min = 5, max = 50, message = "characters should between 5 to 50")
    private String title;

    @FutureOrPresent
    private LocalDateTime startDateTime;

    @Future
    private LocalDateTime endDateTime;

    private long duration;
    private boolean isRandomQuestions;
    private long noOfQuestions;

    private String userGroup;
}
