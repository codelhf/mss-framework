package com.mss.framework.base.user.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.framework.base.user.admin.pojo.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Title: SysRoleMapper
 * @Description: SysRole持久层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-09-16
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

}