package com.klymb.quiz_service.service;

import com.klymb.quiz_service.dto.QuizForParticipantDto;
import com.klymb.quiz_service.entity.Question;
import com.klymb.quiz_service.exception.NotFoundException;
import com.klymb.quiz_service.exception.QuizException;
import com.klymb.quiz_service.mapper.Mapper;
import com.klymb.quiz_service.reposioty.QuestionRepository;
import com.klymb.quiz_service.reposioty.QuizRepository;
import com.klymb.quiz_service.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public QuizForParticipantDto getQuizForParticipant(String id) {
        var quiz = quizRepository.findByIdAndQuestionBankTenantId(id, SecurityUtils.getCurrentUserTenantId())
                .orElseThrow(() -> new NotFoundException("Quiz not found"));

        if(quiz.getCreatedBy().equals(SecurityUtils.getCurrentUserId()))
            throw new QuizException("You created this quiz, so you can't attend it", HttpStatus.BAD_REQUEST);

        List<Question> questions = new ArrayList<>();
        if(quiz.isRandomQuestions()){
            questions.addAll(questionRepository.findRandomQuestionsByQuestionBankId(quiz.getQuestionBank().getId(), quiz.getNoOfQuestions()));
        } else {
            questions.addAll(quiz.getQuestionBank().getQuestions());
        }

        return Mapper.quizToQuizForParticipantDto(quiz, questions);
    }
}
