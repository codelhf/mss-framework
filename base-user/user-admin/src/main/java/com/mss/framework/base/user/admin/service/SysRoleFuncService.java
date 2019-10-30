package com.mss.framework.base.user.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mss.framework.base.core.util.IDUtil;
import com.mss.framework.base.user.admin.common.Constants;
import com.mss.framework.base.user.admin.common.LogType;
import com.mss.framework.base.user.admin.common.RequestHolder;
import com.mss.framework.base.user.admin.dao.SysLogMapper;
import com.mss.framework.base.user.admin.dao.SysRoleFuncMapper;
import com.mss.framework.base.user.admin.pojo.SysLog;
import com.mss.framework.base.user.admin.pojo.SysRoleFunc;
import com.mss.framework.base.user.admin.util.IpUtil;
import com.mss.framework.base.user.admin.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysRoleFuncService {

    @Autowired
    private SysRoleFuncMapper sysRoleFuncMapper;
    @Autowired
    private SysLogMapper sysLogMapper;

    public void changeRoleFunc(String roleId, List<String> funcIdList){
        QueryWrapper<SysRoleFunc> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        List<String> originFuncIdList = sysRoleFuncMapper.selectList(wrapper).stream().map(SysRoleFunc::getFuncId).collect(Collectors.toList());
        if (originFuncIdList.size() == funcIdList.size()){
            Set<String> originFuncIdSet = Sets.newHashSet(originFuncIdList);
            Set<String> funcIdSet = Sets.newHashSet(funcIdList);
            originFuncIdSet.removeAll(funcIdSet);
            if (CollectionUtils.isEmpty(originFuncIdSet)){
                return;
            }
        }
        updateRoleFunc(roleId, funcIdList);
        saveRoleFuncLog(roleId, originFuncIdList, funcIdList);
    }

    @Transactional
    void updateRoleFunc(String roleId, List<String> funcIdList){
        QueryWrapper<SysRoleFunc> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        //先删除角色所有的权限
        sysRoleFuncMapper.delete(wrapper);
        if (CollectionUtils.isEmpty(funcIdList)){
            return;
        }
        List<SysRoleFunc> roleFuncList = Lists.newArrayList();
        for (String funcId : funcIdList){
            SysRoleFunc roleFunc = SysRoleFunc.builder()
                    .id(IDUtil.UUIDStr())
                    .roleId(roleId)
                    .funcId(funcId)
                    .updateUser(RequestHolder.getCurrentUser().getUsername())
                    .updateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .updateTime(new Date())
                    .build();
            roleFuncList.add(roleFunc);
        }
        //再新增选中的权限
        sysRoleFuncMapper.batchInsert(roleFuncList);
    }

    public void saveRoleFuncLog(String roleId, List<String> before, List<String> after){
        SysLog sysLog = new SysLog();
        sysLog.setType(LogType.TYPE_ROLE_FUNC);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2Str(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2Str(after));
        sysLog.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
        sysLog.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setUpdateTime(new Date());
        sysLog.setStatus(Constants.LogStatusEnum.RECOVER.getCode());
        sysLogMapper.insert(sysLog);
    }
}
