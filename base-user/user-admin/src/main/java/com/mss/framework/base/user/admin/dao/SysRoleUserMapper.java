package com.mss.framework.base.user.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.framework.base.user.admin.pojo.SysRoleUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Title: SysRoleUserMapper
 * @Description: SysRoleUser持久层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-09-16
 */
@Mapper
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {

}