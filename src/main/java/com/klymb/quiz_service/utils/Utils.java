package com.klymb.quiz_service.utils;

import com.klymb.quiz_service.entity.enums.DifficultyLevel;
import com.klymb.quiz_service.entity.Question;
import com.klymb.quiz_service.entity.enums.QuestionType;
import com.klymb.quiz_service.exception.FileFormatException;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {
    public static DifficultyLevel parseDifficultyLevel(String value) {
        try {
            return DifficultyLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static QuestionType parseQuestionType(String value) {
        try {
            return QuestionType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Question validateQuestionRowFromExcel(
            String questionText,
            String difficultyLevel,
            Integer mark,
            String options,
            String correctOptions,
            int questionNo
    ) {

        if (questionText == null || questionText.trim().isEmpty())
            throw new FileFormatException(String.format("Question not found for question no %d", questionNo));

        if(difficultyLevel == null || parseDifficultyLevel(difficultyLevel) == null)
            throw new FileFormatException(String.format(
                    "Invalid difficulty method for question no %d, available options: %s",
                    questionNo, Arrays.toString(DifficultyLevel.values())
            ));

        if(mark == null)
            throw new FileFormatException(String.format("Invalid mark for question no %d", questionNo));

        if(options == null || options.trim().isEmpty() || correctOptions == null || correctOptions.trim().isEmpty())
            throw new FileFormatException(String.format("Options or correct options not correct for question no %d", questionNo));

        Set<String> optionsSet = Arrays.stream(options.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
        Set<String> correctOptionsSet = Arrays.stream(correctOptions.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        if(!optionsSet.containsAll(correctOptionsSet))
            throw new FileFormatException(String.format(
                    "Correct options not found in given options for question no %d, options: %s correct: %s, ",
                    questionNo, optionsSet, correctOptionsSet)
            );

        var questionType = correctOptionsSet.size() > 1 ? QuestionType.MULTIPLE : QuestionType.SINGLE;

        return Question.builder()
                .question(questionText.trim())
                .difficultyLevel(parseDifficultyLevel(difficultyLevel))
                .questionType(questionType)
                .mark(mark)
                .options(optionsSet)
                .correctOptions(correctOptionsSet)
                .questionNo(questionNo)
                .build();
    }

    public static String generateRandomCode() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 11; i++) {
            if (i == 3 || i == 7) {
                code.append("-");
            } else {
                int index = random.nextInt(chars.length());
                code.append(chars.charAt(index));
            }
        }

        return code.toString();
    }

    public static String joinCodeWithTenantId(String code, String tenantId) {
        return String.format("%s::::%s", code, tenantId);
    }
}
