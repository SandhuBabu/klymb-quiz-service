package com.klymb.quiz_service.utils;

import com.klymb.quiz_service.entity.Question;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

public class FileUtils {

    public static Set<Question> extractQuestionsFromExcel(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Set<Question> questions = new HashSet<>();
        var rowIndex = -1;
        for (Row row : sheet) {
            if (rowIndex == -1) {
                rowIndex++;
                continue;
            }
            String questionText = row.getCell(0).getStringCellValue();
            String difficultyLevel = row.getCell(1).getStringCellValue();
            int mark = (int)row.getCell(2).getNumericCellValue();

            var optionsCell = row.getCell(3);
            var options = switch (optionsCell.getCellType()) {
                case STRING -> optionsCell.getStringCellValue();
                case NUMERIC -> String.valueOf(optionsCell.getNumericCellValue());
                case BOOLEAN -> String.valueOf(optionsCell.getBooleanCellValue());
                default -> null;
            };

            Cell correctCell = row.getCell(4);
            String correctOptions = correctCell == null ? null : switch (correctCell.getCellType()) {
                case STRING -> correctCell.getStringCellValue();
                case BOOLEAN -> String.valueOf(correctCell.getBooleanCellValue());
                case NUMERIC -> {
                    double val = correctCell.getNumericCellValue();
                    yield (val % 1 == 0) ? String.valueOf((int) val) : String.valueOf(val);
                }
                default -> null;
            };

            var question = Utils.validateQuestionRowFromExcel(
                    questionText,
                    difficultyLevel,
                    mark,
                    options,
                    correctOptions,
                    rowIndex+1
            );
            questions.add(question);
            rowIndex++;
        }

        workbook.close();
        return questions;
    }
}
