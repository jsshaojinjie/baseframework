package com.baseframework.pojo.sysuser;

import lombok.Data;

@Data
public class SysUserLoginInfo {
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;
}
