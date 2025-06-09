package com.klymb.quiz_service.entity;

import com.klymb.quiz_service.entity.enums.QuestionBankStatus;
import com.klymb.quiz_service.entity.enums.QuestionBankType;
import com.klymb.quiz_service.utils.SecurityUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "tenantId"})
})
public class QuestionBank {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 50)
    private String title;

    @Enumerated(EnumType.STRING)
    private QuestionBankType type;

    @Enumerated(EnumType.STRING)
    private QuestionBankStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;
    private String tenantId;

    @ManyToOne
    private TenantKeyValue category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "questionBank", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("questionNo ASC")
    private Set<Question> questions;

    @PrePersist
    protected void onCreate() {
        var now = LocalDateTime.now();
        var currentUser = SecurityUtils.getCurrentUserId();

        this.createdAt = now;
        this.updatedAt = now;
        this.createdBy = currentUser;
        this.updatedBy = currentUser;
        this.tenantId = SecurityUtils.getCurrentUserTenantId();
        this.status = QuestionBankStatus.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = SecurityUtils.getCurrentUserId();
    }

    public String getTypeText() {
        return type.getText();
    }

    public String getStatusText() {
        return status.getText();
    }

}
