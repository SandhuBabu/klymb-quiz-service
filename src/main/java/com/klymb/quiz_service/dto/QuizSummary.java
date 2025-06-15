package com.klymb.quiz_service.dto;

import com.klymb.quiz_service.entity.enums.QuizStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class QuizSummary {
    private String id;
    private String title;
    private Long duration;
    private Long noOfQuestions;
    private boolean isRandomQuestions;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private QuizStatus status;
    private String questionBankTitle;
    private String questionBankId;
    private LocalDateTime updatedAt;
    private String code;
    private String userGroup;

    public String getStatusText() {
        return status.getText();
    }
}
