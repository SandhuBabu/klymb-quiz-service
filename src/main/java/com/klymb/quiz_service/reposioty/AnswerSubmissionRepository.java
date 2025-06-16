package com.klymb.quiz_service.reposioty;

import com.klymb.quiz_service.entity.AnswerSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerSubmissionRepository extends JpaRepository<AnswerSubmission, String> {
}
