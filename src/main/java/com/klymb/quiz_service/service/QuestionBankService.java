package com.klymb.quiz_service.service;

import com.klymb.quiz_service.dto.QuestionBankFormDto;
import com.klymb.quiz_service.entity.Question;
import com.klymb.quiz_service.entity.QuestionBank;
import com.klymb.quiz_service.entity.enums.TenantKey;
import com.klymb.quiz_service.entity.TenantKeyValue;
import com.klymb.quiz_service.entity.projection.QuestionBankSummary;
import com.klymb.quiz_service.exception.NotFoundException;
import com.klymb.quiz_service.exception.QuestionBankException;
import com.klymb.quiz_service.reposioty.QuestionBankRepository;
import com.klymb.quiz_service.reposioty.QuestionRepository;
import com.klymb.quiz_service.reposioty.TenantKeyValueRepository;
import com.klymb.quiz_service.security.UserRole;
import com.klymb.quiz_service.utils.FileUtils;
import com.klymb.quiz_service.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionBankService {

    private final QuestionBankRepository questionBankRepository;
    private final QuestionRepository questionRepository;
    private final TenantKeyValueRepository keyValueRepository;

    private boolean managerAccess(List<String> currentUserRoles){
        return currentUserRoles.contains("ROLE_MANAGER");
    }

    private boolean adminAccess(List<String> currentUserRoles){
        return currentUserRoles.contains("ROLE_ADMIN");
    }

    private boolean superAdminAccess(List<String> currentUserRoles){
        return currentUserRoles.contains("ROLE_SUPER_ADMIN");
    }

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
        var currentUserRoles = SecurityUtils.getCurrentUserRoles();
        if(!(currentUserRoles.contains("ROLE_ADMIN") || currentUserRoles.contains("ROLE_MANAGER")))
            throw new QuestionBankException("Unauthorized"+currentUserRoles, HttpStatus.FORBIDDEN);

        return questionBankRepository.findByIdAndTenantId(id, SecurityUtils.getCurrentUserTenantId())
                .orElseThrow(() -> new NotFoundException("Question bank not found"));
    }

    public List<QuestionBankSummary> getAllQuestionBank() {
        return questionBankRepository.findAllSummariesByTenantId(SecurityUtils.getCurrentUserTenantId());
    }

    @Transactional
    public void deleteQuestionBank(String id) {
        var currentUser = SecurityUtils.getCurrentUserId();
        var currentUserTenant = SecurityUtils.getCurrentUserTenantId();
        var currentUserRoles = SecurityUtils.getCurrentUserRoles();

        questionBankRepository.findByIdAndTenantId(id, currentUserTenant)
                .orElseThrow(() -> new NotFoundException("question bank not found"));

        if(!(currentUserRoles.contains("ROLE_ADMIN") || currentUserRoles.contains("ROLE_MANAGER")))
            throw new QuestionBankException("Unauthorized"+currentUserRoles, HttpStatus.FORBIDDEN);

        questionBankRepository.deleteById(id);
    }
}
