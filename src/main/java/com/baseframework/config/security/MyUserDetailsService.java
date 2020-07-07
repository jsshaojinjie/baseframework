
package com.baseframework.config.security;


import cn.hutool.crypto.SecureUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户详细信息
 */
@Slf4j
@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {


    /**
     * 用户密码登录
     *
     * @param username 用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        return getUserDetails(username);
    }


    /**
     * 构建userdetails
     *
     * @return
     */
    private UserDetails getUserDetails(String username) {
        //TODO 加上redis缓存
        //TODO 查询数据库真是数据
        List<MyGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(MyGrantedAuthority.builder().authority("role1").build());
        return new CurrentUser(1, username, SecureUtil.md5("111111"), true, true, true, true, authorityList);
    }
}
