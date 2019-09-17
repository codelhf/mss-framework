package com.mss.framework.base.user.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.mss.framework.base.user.admin.common.RequestHolder;
import com.mss.framework.base.user.admin.dao.SysFuncMapper;
import com.mss.framework.base.user.admin.dao.SysRoleFuncMapper;
import com.mss.framework.base.user.admin.dao.SysRoleUserMapper;
import com.mss.framework.base.user.admin.pojo.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysCoreService {

    @Autowired
    private SysFuncMapper sysFuncMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysRoleFuncMapper sysRoleFuncMapper;


    public List<SysFunc> getCurrentUserFuncList(){
        String userId = RequestHolder.getCurrentUser().getId();
        return getUserFuncList(userId);
    }

    public List<SysFunc> getRoleFuncList(String roleId){
        QueryWrapper<SysRoleFunc> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        List<String> funcIdList = sysRoleFuncMapper.selectList(wrapper).stream().map(SysRoleFunc::getFuncId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(funcIdList)){
            return Lists.newArrayList();
        }
        return sysFuncMapper.selectBatchIds(funcIdList);
    }

    public List<SysFunc> getUserFuncList(String userId){
        if (isSuperAdmin()){
            return sysFuncMapper.selectList(null);
        }
        QueryWrapper<SysRoleUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<String> roleIdList = sysRoleUserMapper.selectList(wrapper).stream().map(SysRoleUser::getRoleId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        QueryWrapper<SysRoleFunc> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIdList);
        List<String> powerIdList = sysRoleFuncMapper.selectList(queryWrapper).stream().map(SysRoleFunc::getFuncId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(powerIdList)){
            return Lists.newArrayList();
        }
        return sysFuncMapper.selectBatchIds(powerIdList);
    }

    private boolean isSuperAdmin(){
        // 这里是自己定义了一个假的超级管理员
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser.getUsername().contains("admin")){
            return true;
        }
        return false;
    }

    public boolean hasUrlACL(String url){
        if (isSuperAdmin()){
            return true;
        }
        QueryWrapper<SysFunc> wrapper = new QueryWrapper<>();
        wrapper.eq("url", url);
        List<SysFunc> powerList = sysFuncMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(powerList)){
            return true;
        }
        List<SysFunc> userAclList = getCurrentUserFuncList();
        Set<String> userAclIdSet = userAclList.stream().map(SysFunc::getId).collect(Collectors.toSet());
        boolean hasValidAcl = false;
        // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
        for (SysFunc acl : powerList){
            // 判断一个用户是否具有某个权限点的访问权限
            if (acl == null || acl.getStatus() != 1){
                continue;
            }
            hasValidAcl = true;
            if (userAclIdSet.contains(acl.getId())){
                return true;
            }
        }
        if (!hasValidAcl){
            return true;
        }
        return true;
    }
}
