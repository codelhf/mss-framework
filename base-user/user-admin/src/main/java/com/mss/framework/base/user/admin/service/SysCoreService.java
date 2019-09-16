package com.mss.framework.base.user.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.mss.framework.base.user.admin.beans.CacheKeyConstants;
import com.mss.framework.base.user.admin.common.RequestHolder;
import com.mss.framework.base.user.admin.dao.SysPowerMapper;
import com.mss.framework.base.user.admin.dao.SysRolePowerMapper;
import com.mss.framework.base.user.admin.dao.SysRoleUserMapper;
import com.mss.framework.base.user.admin.pojo.*;
import com.mss.framework.base.user.admin.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysCoreService {

    @Autowired
    private SysPowerMapper sysPowerMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysRolePowerMapper sysRolePowerMapper;


    public List<SysPower> getCurrentUserAclList(){
        String userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    public List<SysPower> getRoleAclList(String roleId){
        QueryWrapper<SysRolePower> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        List<String> aclIdList = sysRolePowerMapper.selectList(wrapper).stream().map(SysRolePower::getPowerId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(aclIdList)){
            return Lists.newArrayList();
        }
        return sysPowerMapper.selectBatchIds(aclIdList);
    }

    public List<SysPower> getUserAclList(String userId){
        if (isSuperAdmin()){
            return sysPowerMapper.selectList(null);
        }
        QueryWrapper<SysRoleUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<String> roleIdList = sysRoleUserMapper.selectList(wrapper).stream().map(SysRoleUser::getRoleId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        QueryWrapper<SysRolePower> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIdList);
        List<String> powerIdList = sysRolePowerMapper.selectList(queryWrapper).stream().map(SysRolePower::getPowerId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(powerIdList)){
            return Lists.newArrayList();
        }
        return sysPowerMapper.selectBatchIds(powerIdList);
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
        QueryWrapper<SysPower> wrapper = new QueryWrapper<>();
        wrapper.eq("url", url);
        List<SysPower> powerList = sysPowerMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(powerList)){
            return true;
        }
        List<SysPower> userAclList = getCurrentUserAclList();
        Set<String> userAclIdSet = userAclList.stream().map(SysPower::getId).collect(Collectors.toSet());
        boolean hasValidAcl = false;
        // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
        for (SysPower acl : powerList){
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
