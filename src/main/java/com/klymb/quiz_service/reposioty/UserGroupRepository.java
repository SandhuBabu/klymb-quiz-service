package com.klymb.quiz_service.reposioty;

import com.klymb.quiz_service.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, String>, JpaSpecificationExecutor<UserGroup> {
    Optional<UserGroup> findByIdAndTenantId(String id, String tenantId);
}
