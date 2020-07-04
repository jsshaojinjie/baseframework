package com.baseframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baseframework.entity.SysUser;
import com.baseframework.mapper.SysUserMapper;
import com.baseframework.service.ISysUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
