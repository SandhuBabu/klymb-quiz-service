package com.klymb.quiz_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"questionBank"})
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String question;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private QuestionType questionType;

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option")
    private Set<String> options = new HashSet<>();

    private int mark;

    @ElementCollection
    @CollectionTable(name = "question_correct_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "correct_option")
    private Set<String> correctOptions = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_bank_id")
    private QuestionBank questionBank;

    public String getDifficultyLevelText() {
        return difficultyLevel.getText();
    }

    public String getQuestionTypeText() {
        return questionType.getText();
    }
}
