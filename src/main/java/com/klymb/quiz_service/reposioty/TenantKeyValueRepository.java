package com.klymb.quiz_service.reposioty;

import com.klymb.quiz_service.entity.enums.TenantKey;
import com.klymb.quiz_service.entity.TenantKeyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TenantKeyValueRepository extends JpaRepository<TenantKeyValue, String> {
    Set<TenantKeyValue> findAllByKeyAndTenantId(TenantKey key, String tenantId);
    Optional<TenantKeyValue> findByIdAndTenantId(String id, String tenantId);
}
