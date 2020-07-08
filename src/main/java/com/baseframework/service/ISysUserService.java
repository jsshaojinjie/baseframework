
package com.baseframework.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baseframework.entity.SysUser;
import com.baseframework.pojo.sysuser.SysUserLoginInfo;

public interface ISysUserService extends IService<SysUser> {
    SysUserLoginInfo findByUsername(String username);
}
