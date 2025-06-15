package com.klymb.quiz_service.controller;

import com.klymb.quiz_service.dto.QuestionBankSummary;
import com.klymb.quiz_service.entity.UserGroup;
import com.klymb.quiz_service.entity.enums.QuestionBankStatus;
import com.klymb.quiz_service.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-group")
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupService usersGroupService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<UserGroup> createUserGroup(@RequestBody UserGroup userGroup) {
        var response = usersGroupService.createUserGroup(userGroup);
        return ResponseEntity.status(201).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping
    public List<UserGroup> getQuestionsBanks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean enabled
    ) {
        return usersGroupService.getAllUserGroup(title, enabled);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PatchMapping("/{id}/status")
    public void toggleStatus(@PathVariable String id) {
        usersGroupService.toggleStatus(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping("/{id}")
    public UserGroup getOneUserGroup(@PathVariable String id) {
        return usersGroupService.getOneUserGroup(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PatchMapping("/{id}")
    public UserGroup updateUserGroup(@PathVariable String id, @RequestBody UserGroup userGroup) {
        return usersGroupService.updateUserGroup(id, userGroup);
    }
}
