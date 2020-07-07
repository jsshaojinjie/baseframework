package com.baseframework.controller;

import com.baseframework.config.security.CurrentUser;
import com.baseframework.entity.SysUser;
import com.baseframework.mapper.SysUserMapper;
import com.baseframework.service.ISysUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController extends BaseController {

    private final ISysUserService sysUserService;


    @PostMapping("/info/{id}")
    public SysUser info(@PathVariable Integer id) {
        CurrentUser user = getCurrentUser();
        return this.sysUserService.getById(id);
    }
}
