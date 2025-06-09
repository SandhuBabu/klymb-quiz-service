package com.klymb.quiz_service.entity;

import com.klymb.quiz_service.entity.enums.TenantKey;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TenantKeyValue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private TenantKey key;
    private String value;
    private String tenantId;


}
