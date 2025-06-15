package com.klymb.quiz_service.reposioty;

import com.klymb.quiz_service.dto.QuizSummary;
import com.klymb.quiz_service.entity.Quiz;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {
    List<Quiz> findAllByQuestionBankTenantId(String tenantId);

    Optional<Quiz> findByIdAndQuestionBankTenantId(String id, String tenantId);

    @Modifying
    @Transactional
    @Query(value = """
              UPDATE quiz
              SET status = CASE
                WHEN CURRENT_TIMESTAMP BETWEEN start_date_time AND end_date_time THEN 'LIVE'
                WHEN CURRENT_TIMESTAMP > end_date_time THEN 'COMPLETED'
                ELSE status
              END
              WHERE status NOT IN ('CANCELLED', 'COMPLETED')
            """, nativeQuery = true)
    int updateQuizStatusToLiveOrCompleted();

}
