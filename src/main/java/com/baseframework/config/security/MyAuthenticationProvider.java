package com.baseframework.config.security;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    /**
     * 自定义验证方式
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user.getPassword().equals(password)) {
            return new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
        } else {
            throw new UsernameNotFoundException("用户名密码不正确");
        }
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

}