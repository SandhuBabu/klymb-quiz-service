package com.klymb.quiz_service.dto;

import com.klymb.quiz_service.entity.QuestionBankType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class QuestionBankFormDto {
    private MultipartFile file;

    @NotNull(message = "please enter a valid category")
    @NotBlank(message = "please enter a valid category")
    private String category;

    @NotNull(message = "please enter a title")
    @NotBlank(message = "please enter a title")
    @Size(max = 50, min = 3, message = "No of characters in title is between 3 to 50")
    private String title;

    @NotNull(message = "please enter a valid question bank type")
    private QuestionBankType type;
}
