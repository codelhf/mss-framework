package com.mss.framework.base.user.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.framework.base.user.admin.pojo.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title: SysDeptMapper
 * @Description: SysUserGroup持久层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-09-16
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

    int batchUpdateLevel(@Param("deptList") List<SysDept> deptList);
}