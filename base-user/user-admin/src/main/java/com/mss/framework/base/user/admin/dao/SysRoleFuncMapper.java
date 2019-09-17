package com.mss.framework.base.user.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.framework.base.user.admin.pojo.SysRoleFunc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title: SysRoleFuncMapper
 * @Description: SysRolePower持久层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-09-16
 */
@Mapper
public interface SysRoleFuncMapper extends BaseMapper<SysRoleFunc> {

    int batchInsert(@Param("roleFuncList") List<SysRoleFunc> roleFuncList);
}