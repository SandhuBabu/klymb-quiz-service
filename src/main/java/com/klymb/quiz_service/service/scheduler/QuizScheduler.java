package com.klymb.quiz_service.service.scheduler;

import com.klymb.quiz_service.reposioty.QuizRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizScheduler {

    private final QuizRepository quizRepository;

    @Scheduled(fixedRate = 30000) // Every 30 seconds
    @Transactional
    public void updateQuizStatusesScheduler() {
        int count = quizRepository.updateQuizStatusToLiveOrCompleted();
        System.out.println("status updated: " + count);
    }
}
