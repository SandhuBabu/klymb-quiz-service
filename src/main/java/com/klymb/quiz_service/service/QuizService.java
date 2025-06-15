package com.klymb.quiz_service.service;

import com.klymb.quiz_service.dto.QuizFormDto;
import com.klymb.quiz_service.dto.QuizReScheduleFormDto;
import com.klymb.quiz_service.dto.QuizSummary;
import com.klymb.quiz_service.entity.Quiz;
import com.klymb.quiz_service.entity.enums.QuizStatus;
import com.klymb.quiz_service.exception.NotFoundException;
import com.klymb.quiz_service.exception.QuestionBankException;
import com.klymb.quiz_service.exception.UserGroupException;
import com.klymb.quiz_service.mapper.Mapper;
import com.klymb.quiz_service.reposioty.QuestionBankRepository;
import com.klymb.quiz_service.reposioty.QuizRepository;
import com.klymb.quiz_service.reposioty.UserGroupRepository;
import com.klymb.quiz_service.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionBankRepository questionBankRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public Quiz createQuiz(QuizFormDto dto) {
        var tenantId = SecurityUtils.getCurrentUserTenantId();
        var questionBank = questionBankRepository.findByIdAndTenantId(dto.getQuestionBankId(), tenantId)
                .orElseThrow(() -> new QuestionBankException("Question bank not found", HttpStatus.NOT_FOUND));

        var userGroup = userGroupRepository.findByIdAndTenantId(dto.getUserGroup(), tenantId)
                .orElseThrow(() -> new NotFoundException("User group not found"));

        if(!userGroup.getIsEnabled())
            throw new UserGroupException("User group is not active", HttpStatus.BAD_REQUEST);

        Quiz quiz = Mapper.quizFormDtoToQuiz(dto, questionBank, userGroup);
        return quizRepository.save(quiz);
    }

    public List<QuizSummary> getAllQuiz() {
        var quizzes = quizRepository.findAllByQuestionBankTenantId(SecurityUtils.getCurrentUserTenantId());
        return quizzes.stream()
                .map(Mapper::quizToSummary)
                .collect(Collectors.toList());
    }

    public Quiz getQuizDetail(String id) {
        var quiz = quizRepository.findByIdAndQuestionBankTenantId(id, SecurityUtils.getCurrentUserTenantId())
                .orElseThrow(() -> new NotFoundException("Quiz not found"));
        if(quiz.isRandomQuestions())
            quiz.getQuestionBank().setQuestions(Set.of());
        return quiz;
    }

    @Transactional
    public void cancelQuiz(String id) {
        var quiz = quizRepository.findByIdAndQuestionBankTenantId(id, SecurityUtils.getCurrentUserTenantId())
                .orElseThrow(() -> new NotFoundException("Quiz not found"));
        quiz.setStatus(QuizStatus.CANCELLED);
        quizRepository.save(quiz);
    }

    @Transactional
    public Quiz reSchedule(String id, QuizReScheduleFormDto dto) {
        var quiz = quizRepository.findByIdAndQuestionBankTenantId(id, SecurityUtils.getCurrentUserTenantId())
                .orElseThrow(() -> new NotFoundException("Quiz not found"));
        quiz.setStatus(QuizStatus.RE_SCHEDULED);
        quiz.setStartDateTime(dto.getStartDateTime());
        quiz.setEndDateTime(dto.getEndDateTime());
        quiz.setDuration(dto.getDuration());

        return quizRepository.save(quiz);
    }

    public void deleteQuiz(String id) {
        var quiz = quizRepository.findByIdAndQuestionBankTenantId(id, SecurityUtils.getCurrentUserTenantId())
                .orElseThrow(() -> new NotFoundException("Quiz not found"));
        quizRepository.delete(quiz);
    }
}
