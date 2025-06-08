package com.klymb.quiz_service.reposioty;

import com.klymb.quiz_service.entity.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, String> {
    Optional<QuestionBank> findByIdAndTenantId(String id, String tenantId);
}
