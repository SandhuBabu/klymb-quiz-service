package com.klymb.quiz_service.dto;

import com.klymb.quiz_service.entity.TenantKeyValue;
import com.klymb.quiz_service.entity.enums.QuestionBankStatus;
import com.klymb.quiz_service.entity.enums.QuestionBankType;
import com.klymb.quiz_service.entity.enums.QuestionType;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Data
@Builder
public class QuestionBankSummary {
    private String id;
    private String title;
    private LocalDateTime updatedAt;
    private QuestionBankType type;
    private QuestionBankStatus status;
    private TenantKeyValue category;
    private int questionCount;

    public String getTypeText() {
        return type.getText();
    }

    public String getStatusText() {
        return status.getText();
    }
}

