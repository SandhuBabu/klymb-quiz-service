package com.klymb.quiz_service.mapper;

import com.klymb.quiz_service.dto.QuizFormDto;
import com.klymb.quiz_service.dto.QuizSummary;
import com.klymb.quiz_service.entity.QuestionBank;
import com.klymb.quiz_service.dto.QuestionBankSummary;
import com.klymb.quiz_service.entity.Quiz;
import com.klymb.quiz_service.entity.UserGroup;

public class Mapper {
    public static QuestionBankSummary questionBankToSummary(QuestionBank questionBank) {
        return QuestionBankSummary.builder()
                .id(questionBank.getId())
                .title(questionBank.getTitle())
                .category(questionBank.getCategory())
                .type(questionBank.getType())
                .status(questionBank.getStatus())
                .updatedAt(questionBank.getUpdatedAt())
                .questionCount(questionBank.getQuestions().size())
                .build();
    }

    public static Quiz quizFormDtoToQuiz(QuizFormDto dto, QuestionBank questionBank, UserGroup userGroup) {
        System.out.println(dto);
        var quiz = Quiz.builder()
                .title(dto.getTitle())
                .duration(dto.getDuration())
                .startDateTime(dto.getStartDateTime())
                .endDateTime(dto.getEndDateTime())
                .questionBank(questionBank)
                .isRandomQuestions(dto.isRandomQuestions())
                .userGroup(userGroup)
                .build();

        if(dto.isRandomQuestions()) {
            quiz.setNoOfQuestions(dto.getNoOfQuestions());
        }
        System.out.println(dto.isRandomQuestions()+", from entity: "+quiz.isRandomQuestions());

        return quiz;
    }

    public static QuizSummary quizToSummary(Quiz quiz) {
        var noOfQuestions = quiz.isRandomQuestions() ? quiz.getNoOfQuestions() : quiz.getQuestionBank().getQuestions().size();
        return QuizSummary.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .duration(quiz.getDuration())
                .startDateTime(quiz.getStartDateTime())
                .endDateTime(quiz.getEndDateTime())
                .noOfQuestions(noOfQuestions)
                .code(quiz.getCode())
                .userGroup(quiz.getUserGroup().getTitle())
                .isRandomQuestions(quiz.isRandomQuestions())
                .status(quiz.getStatus())
                .updatedAt(quiz.getUpdatedAt())
                .questionBankId(quiz.getQuestionBank().getId())
                .questionBankTitle(quiz.getQuestionBank().getTitle())
                .build();
    }
}
