package com.klymb.quiz_service.reposioty;


import com.klymb.quiz_service.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {

    List<Question> findByQuestionBankIdAndQuestionBankTenantId(String questionBankId, String tenantId);

    @Query(
            value = "SELECT * FROM question WHERE question_bank_id = :questionBankId ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true
    )
    List<Question> findRandomQuestionsByQuestionBankId(
            @Param("questionBankId") String questionBankId,
            @Param("limit") long limit
    );

}
