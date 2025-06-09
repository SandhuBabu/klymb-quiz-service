package com.klymb.quiz_service.entity.projection;

import com.klymb.quiz_service.entity.TenantKeyValue;
import com.klymb.quiz_service.entity.enums.QuestionBankStatus;
import com.klymb.quiz_service.entity.enums.QuestionBankType;
import com.klymb.quiz_service.entity.enums.QuestionType;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface QuestionBankSummary {
    String getId();
    String getTitle();
    LocalDateTime getUpdatedAt();
    QuestionBankType getType();
    QuestionBankStatus getStatus();
    CategoryInfo getCategory();
    int getQuestionCount();

    default String getTypeText() {
        return getType().getText();
    }

    default String getStatusText() {
        return getStatus().getText();
    }

    interface CategoryInfo {
        String getId(); // adjust fields as per your TenantKeyValue
        String getKey();
        String getValue();
    }
}
