package com.klymb.quiz_service.reposioty;

import com.klymb.quiz_service.entity.QuestionBank;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionBankRepository extends
        JpaRepository<QuestionBank, String>, JpaSpecificationExecutor<QuestionBank> {
    Optional<QuestionBank> findByIdAndTenantId(String id, String tenantId);

}
