package com.projects.socialapp.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminOnlyAspect {

    private static final Logger log = LogManager.getLogger(AdminOnlyAspect.class);

    @Before("@annotation(com.projects.socialapp.aspect.AdminOnly)")
    public void checkAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication);

        // Check if the user has the ADMIN role
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
            throw new SecurityException("Access denied: You must be an ADMIN to perform this action.");
        }
    }
}
