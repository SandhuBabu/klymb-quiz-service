package com.klymb.quiz_service.entity;

import com.klymb.quiz_service.entity.enums.QuestionBankStatus;
import com.klymb.quiz_service.entity.enums.QuizStatus;
import com.klymb.quiz_service.utils.SecurityUtils;
import com.klymb.quiz_service.utils.Utils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 50)
    private String title;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long duration;

    @ManyToOne
    private QuestionBank questionBank;

    @Enumerated(EnumType.STRING)
    private QuizStatus status;

    private boolean isRandomQuestions;

    private long noOfQuestions;

    @Column(unique = true)
    private String code;

    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    private UserGroup userGroup;

    @PrePersist
    protected void onCreate() {
        var now = LocalDateTime.now();
        var currentUser = SecurityUtils.getCurrentUserId();

        this.createdAt = now;
        this.updatedAt = now;
        this.createdBy = currentUser;
        this.updatedBy = currentUser;
        this.code = Utils.generateRandomCode()+"::::"+SecurityUtils.getCurrentUserTenantId();
        this.status = QuizStatus.SCHEDULED;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = SecurityUtils.getCurrentUserId();
    }

    public Long getNoOfQuestions() {
        if(isRandomQuestions)
            return noOfQuestions;
        return Long.valueOf(String.valueOf(questionBank.getQuestions().size()));
    }

    public String getCode() {
        return this.code.split("::::")[0];
    }

    public QuizStatus getStatus() {
        var now = LocalDateTime.now();

        if(now.isAfter(endDateTime))
            return QuizStatus.COMPLETED;

        if(
                (now.isEqual(startDateTime) || now.isAfter(startDateTime)) &&
                (now.isEqual(endDateTime)   || now.isBefore(endDateTime))
        )
            return QuizStatus.LIVE;
        return status;
    }

    public String getStatusText() {
        return status.getText();
    }
}
