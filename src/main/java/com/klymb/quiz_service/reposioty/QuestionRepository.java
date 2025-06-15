package com.klymb.quiz_service.reposioty;


import com.klymb.quiz_service.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {

    List<Question> findByQuestionBankIdAndQuestionBankTenantId(String questionBankId, String tenantId);

}
