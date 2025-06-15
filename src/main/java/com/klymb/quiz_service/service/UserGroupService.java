package com.klymb.quiz_service.service;

import com.klymb.quiz_service.entity.UserGroup;
import com.klymb.quiz_service.entity.specfications.CustomSpecifications;
import com.klymb.quiz_service.exception.NotFoundException;
import com.klymb.quiz_service.reposioty.UserGroupRepository;
import com.klymb.quiz_service.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserGroupService {

    private final UserGroupRepository usersGroupRepository;

    @Transactional
    public UserGroup createUserGroup(UserGroup userGroup) {
        var emailSetFormatted = userGroup.getEmails()
                .stream()
                .map(String::trim)
                .collect(Collectors.toSet());
        userGroup.setEmails(emailSetFormatted);
        return usersGroupRepository.save(userGroup);
    }

    public List<UserGroup> getAllUserGroup(String title, Boolean isEnabled) {
        Specification<UserGroup> spec = CustomSpecifications.userGroupSpecification(
                SecurityUtils.getCurrentUserTenantId(),
                title,
                isEnabled
        );
        return usersGroupRepository.findAll(spec);
    }

    @Transactional
    public void toggleStatus(String id) {
        var group= usersGroupRepository.findByIdAndTenantId(id, SecurityUtils.getCurrentUserTenantId())
                .orElseThrow(() -> new NotFoundException("User group not found"));
        group.setIsEnabled(!group.getIsEnabled());
        usersGroupRepository.save(group);
    }

    public UserGroup getOneUserGroup(String id) {
        return usersGroupRepository.findByIdAndTenantId(id, SecurityUtils.getCurrentUserTenantId())
                .orElseThrow(() -> new NotFoundException("User group not found"));
    }

    @Transactional
    public UserGroup updateUserGroup(String id, UserGroup userGroup) {
        var group= usersGroupRepository.findByIdAndTenantId(id, SecurityUtils.getCurrentUserTenantId())
                .orElseThrow(() -> new NotFoundException("User group not found"));
        group.setTitle(userGroup.getTitle().trim());
        var emailSetFormatted = userGroup.getEmails()
                .stream()
                .map(String::trim)
                .collect(Collectors.toSet());
        group.setEmails(emailSetFormatted);
        return usersGroupRepository.save(group);
    }
}
