package com.klymb.quiz_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.klymb.quiz_service.entity.enums.AttemptStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany
    @JsonIgnore
    private List<Question> questions;

    @ManyToOne
    private Quiz quiz;

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AnswerSubmission> answerSubmissions;

    @Enumerated(EnumType.STRING)
    private AttemptStatus attemptStatus;

    private int score;
    private String userId;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
