package com.klymb.quiz_service.service;

import com.klymb.quiz_service.dto.QuestionBankFormDto;
import com.klymb.quiz_service.entity.Question;
import com.klymb.quiz_service.entity.QuestionBank;
import com.klymb.quiz_service.entity.TenantKey;
import com.klymb.quiz_service.entity.TenantKeyValue;
import com.klymb.quiz_service.exception.NotFoundException;
import com.klymb.quiz_service.reposioty.QuestionBankRepository;
import com.klymb.quiz_service.reposioty.QuestionRepository;
import com.klymb.quiz_service.reposioty.TenantKeyValueRepository;
import com.klymb.quiz_service.utils.FileUtils;
import com.klymb.quiz_service.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionBankService {

    private final QuestionBankRepository questionBankRepository;
    private final QuestionRepository questionRepository;
    private final TenantKeyValueRepository keyValueRepository;

    @Transactional
    public QuestionBank createQuestion(QuestionBankFormDto dto) throws IOException {
        var questionBankCategory = keyValueRepository.findByIdAndTenantId(dto.getCategory(), SecurityUtils.getCurrentUserTenantId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        var questionBank = QuestionBank.builder()
                .title(dto.getTitle())
                .type(dto.getType())
                .category(questionBankCategory)
                .build();
        QuestionBank savedQuestionBank = questionBankRepository.save(questionBank);

        Set<Question> questions = FileUtils.extractQuestionsFromExcel(dto.getFile());
        questions.stream()
                .peek(question -> question.setQuestionBank(savedQuestionBank))
                .collect(Collectors.toSet());
        questions = new HashSet<>(questionRepository.saveAll(questions));
        savedQuestionBank.setQuestions(new HashSet<>(questions));
        return savedQuestionBank;
    }

    public Set<TenantKeyValue> getQuestionBankCategories() {
        var tenantId = SecurityUtils.getCurrentUserTenantId();
        return keyValueRepository.findAllByKeyAndTenantId(TenantKey.QUESTION_BANK_CATEGORY, tenantId);
    }

    public QuestionBank getQuestionBankById(String id) {
        return questionBankRepository.findByIdAndTenantId(id, SecurityUtils.getCurrentUserTenantId())
                .orElseThrow(() -> new NotFoundException("Question bank not found"));
    }
}
