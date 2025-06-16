package com.klymb.quiz_service.dto;

import com.klymb.quiz_service.entity.enums.QuestionType;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class QuestionForParticipantDto {
    private String id;
    private String question;
    private QuestionType questionType;
    private Set<String> options;
}
