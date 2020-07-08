package com.baseframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baseframework.entity.SysUser;
import com.baseframework.mapper.SysUserMapper;
import com.baseframework.pojo.sysuser.SysUserLoginInfo;
import com.baseframework.service.ISysUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    @Cacheable(value = "user_info", key = "#username", unless = "#result == null")
    public SysUserLoginInfo findByUsername(String username) {
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if (user != null) {
            SysUserLoginInfo loginInfo = new SysUserLoginInfo();
            BeanUtils.copyProperties(user, loginInfo);
            return loginInfo;
        }
        return null;
    }
}
