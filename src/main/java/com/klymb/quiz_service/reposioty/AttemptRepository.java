package com.klymb.quiz_service.reposioty;

import com.klymb.quiz_service.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt,String> {
    Optional<Attempt> findByUserIdAndQuizId(String userId, String quizId);
}
