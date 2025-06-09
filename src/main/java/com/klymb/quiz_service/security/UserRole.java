package com.klymb.quiz_service.security;

public enum UserRole {
    PARTICIPANT("Participant"),
    ADMIN("Admin"),
    SUPER_ADMIN("Super Admin"),
    MANAGER("Manager");

    private String text;

    UserRole(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
