package com.baseframework.config.security;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

@Builder
public class MyGrantedAuthority implements GrantedAuthority {
    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
