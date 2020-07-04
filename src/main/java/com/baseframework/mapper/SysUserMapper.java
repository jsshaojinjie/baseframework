package com.baseframework.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baseframework.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Select("select * from sys_user where id = #{id}")
    SysUser get(Integer id);
}
