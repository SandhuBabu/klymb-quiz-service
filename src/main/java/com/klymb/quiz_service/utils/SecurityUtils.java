package com.klymb.quiz_service.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public class SecurityUtils {

    private SecurityUtils() {
    } // prevent instantiation

    public static Jwt getJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }
        return null;
    }

    public static String getCurrentUserId() {
        Jwt jwt = getJwt();
        return jwt != null ? jwt.getClaimAsString("userId") : null;
    }

    public static String getCurrentUserEmail() {
        Jwt jwt = getJwt();
        return jwt != null ? jwt.getClaimAsString("email") : null;
    }

    public static List<String> getCurrentUserRoles() {
        Jwt jwt = getJwt();
        return jwt != null ? jwt.getClaimAsStringList("roles") : List.of();
    }

    public static String getCurrentUserTenantId() {
        Jwt jwt = getJwt();
        return jwt != null ? jwt.getClaimAsString("tenantId") : null;
    }
}

