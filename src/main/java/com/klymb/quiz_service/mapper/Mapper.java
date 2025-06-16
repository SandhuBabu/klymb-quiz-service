package com.klymb.quiz_service.mapper;

import com.klymb.quiz_service.dto.*;
import com.klymb.quiz_service.entity.Question;
import com.klymb.quiz_service.entity.QuestionBank;
import com.klymb.quiz_service.entity.Quiz;
import com.klymb.quiz_service.entity.UserGroup;

import java.util.List;
import java.util.stream.Collectors;

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

    public static QuestionForParticipantDto questionToQuestionForParticipant(Question question) {
        return QuestionForParticipantDto.builder()
                .id(question.getId())
                .question(question.getQuestion())
                .options(question.getOptions())
                .questionType(question.getQuestionType())
                .build();
    }

    public static QuizForParticipantDto quizToQuizForParticipantDto(Quiz quiz, List<Question> questions) {
        var questionsForParticipant = questions.stream()
                .map(Mapper::questionToQuestionForParticipant)
                .collect(Collectors.toSet());

        return QuizForParticipantDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .duration(quiz.getDuration())
                .noOfQuestions(quiz.getNoOfQuestions())
                .status(quiz.getStatus())
                .questions(questionsForParticipant)
                .build();
    }
}
