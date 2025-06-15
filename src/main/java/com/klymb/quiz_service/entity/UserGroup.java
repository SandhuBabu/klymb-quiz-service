package com.klymb.quiz_service.entity;

import com.klymb.quiz_service.utils.SecurityUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;

    @ElementCollection
    private Set<String> emails;

    private String tenantId;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    private Boolean isEnabled;

    @PrePersist
    protected void onCreate() {
        var now = LocalDateTime.now();
        var currentUser = SecurityUtils.getCurrentUserId();

        this.createdAt = now;
        this.updatedAt = now;
        this.createdBy = currentUser;
        this.updatedBy = currentUser;
        this.isEnabled = true;
        this.tenantId = SecurityUtils.getCurrentUserTenantId();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = SecurityUtils.getCurrentUserId();
    }

}
