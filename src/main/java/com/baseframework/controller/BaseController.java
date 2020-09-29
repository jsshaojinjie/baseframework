package com.baseframework.controller;

import com.baseframework.config.security.CurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 邵锦杰
 */
public class BaseController {
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    public CurrentUser getCurrentUser() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof CurrentUser) {
            return (CurrentUser) principal;
        }
        return null;
    }


}
