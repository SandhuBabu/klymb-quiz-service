package com.klymb.quiz_service.service;

import com.klymb.quiz_service.dto.QuizFormDto;
import com.klymb.quiz_service.dto.QuizReScheduleFormDto;
import com.klymb.quiz_service.dto.QuizSummary;
import com.klymb.quiz_service.entity.Quiz;
import com.klymb.quiz_service.entity.enums.QuizStatus;
import com.klymb.quiz_service.exception.NotFoundException;
import com.klymb.quiz_service.exception.QuestionBankException;
import com.klymb.quiz_service.exception.QuizException;
import com.klymb.quiz_service.exception.UserGroupException;
import com.klymb.quiz_service.mapper.Mapper;
import com.klymb.quiz_service.reposioty.QuestionBankRepository;
import com.klymb.quiz_service.reposioty.QuizRepository;
import com.klymb.quiz_service.reposioty.UserGroupRepository;
import com.klymb.quiz_service.utils.SecurityUtils;
import com.klymb.quiz_service.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Transactional
    public void deleteQuiz(String id) {
        var quiz = quizRepository.findByIdAndQuestionBankTenantId(id, SecurityUtils.getCurrentUserTenantId())
                .orElseThrow(() -> new NotFoundException("Quiz not found"));
        quizRepository.delete(quiz);
    }

    public Map<String, String> joinQuiz(String code) {
        var tenant = SecurityUtils.getCurrentUserTenantId();
        var currentUserEmail = SecurityUtils.getCurrentUserEmail();
        var formattedCode = Utils.joinCodeWithTenantId(code, tenant);

        var quiz = quizRepository.findByCode(formattedCode)
                .orElseThrow(() -> new QuizException("Quiz not found with this code", HttpStatus.NOT_FOUND));

        var usersEmails = quiz.getUserGroup().getEmails();
        if(!usersEmails.contains(currentUserEmail)) {
            throw new QuizException("You don't have access to this quiz", HttpStatus.FORBIDDEN);
        }

        if(List.of(QuizStatus.CANCELLED, QuizStatus.COMPLETED).contains(quiz.getStatus()))
            throw new QuizException("Quiz %s".formatted(quiz.getStatus().getText()), HttpStatus.CONFLICT);

        if(!(quiz.getStatus().equals(QuizStatus.LIVE)))
            throw new QuizException("Quiz is not live, please wait", HttpStatus.CONFLICT);

        return Map.of("title", quiz.getTitle(), "id", quiz.getId());
    }
}
