package com.klymb.quiz_service.reposioty;

import com.klymb.quiz_service.entity.QuestionBank;
import com.klymb.quiz_service.entity.projection.QuestionBankSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, String> {
    Optional<QuestionBank> findByIdAndTenantId(String id, String tenantId);

    @Query("""
                SELECT q.id AS id,
                       q.title AS title,
                       q.type AS type,
                       q.status AS status,
                       q.category AS category,
                       q.updatedAt as updatedAt,
                       SIZE(q.questions) AS questionCount
                FROM QuestionBank q
                WHERE q.tenantId = :tenantId
            """)
    List<QuestionBankSummary> findAllSummariesByTenantId(@Param("tenantId") String tenantId);

}
