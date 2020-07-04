package com.baseframework.comtroller;

import com.baseframework.entity.SysUser;
import com.baseframework.mapper.SysUserMapper;
import com.baseframework.service.ISysUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final ISysUserService sysUserService;
    private final SysUserMapper sysUserMapper;

    @GetMapping("/info/{id}")
    public SysUser info(@PathVariable Integer id) {
        return this.sysUserMapper.selectById(id);
    }
}
