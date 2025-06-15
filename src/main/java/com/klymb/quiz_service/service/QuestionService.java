package com.klymb.quiz_service.service;

import com.klymb.quiz_service.entity.Question;
import com.klymb.quiz_service.reposioty.QuestionRepository;
import com.klymb.quiz_service.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;


    public List<Question> getAllQuestionsForQuestionBank(String qbId) {
        return questionRepository.findByQuestionBankIdAndQuestionBankTenantId(qbId, SecurityUtils.getCurrentUserTenantId());
    }
}
