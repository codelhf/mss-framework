package com.mss.framework.base.user.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.framework.base.user.admin.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Title: SysUserMapper
 * @Description: SysUser持久层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-09-16 15:42:33
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}