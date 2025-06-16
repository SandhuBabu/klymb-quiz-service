package com.klymb.quiz_service.dto;

import com.klymb.quiz_service.entity.enums.QuizStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class QuizForParticipantDto {
    private String id;
    private String title;
    private long duration;
    private long noOfQuestions;
    private QuizStatus status;
    private Set<QuestionForParticipantDto> questions;


    public String getStatusText() {
        return status.getText();
    }
}
