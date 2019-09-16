package com.mss.framework.base.user.admin.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.admin.beans.LogType;
import com.mss.framework.base.user.admin.common.RequestHolder;
import com.mss.framework.base.user.admin.dao.SysLogMapper;
import com.mss.framework.base.user.admin.dao.SysRolePowerMapper;
import com.mss.framework.base.user.admin.util.IpUtil;
import com.mss.framework.base.user.admin.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class SysRolePowerService {

    @Resource
    private SysRolePowerMapper sysRolePowerMapper;
    @Resource
    private SysLogMapper sysLogMapper;

    public ServerResponse changeRoleAcls(String roleId, List<Integer> aclIdList){
        List<Integer> originAclIdList = sysRolePowerMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (originAclIdList.size() == aclIdList.size()){
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdSet)){
                return;
            }
        }
        updateRoleAcls(roleId, aclIdList);
        saveRoleAclLog(roleId, originAclIdList, aclIdList);
    }

    @Transactional
    void updateRoleAcls(int roleId, List<Integer> aclIdList){
        sysRolePowerMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(aclIdList)){
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId : aclIdList){
            SysRoleAcl roleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operateTime(new Date())
                    .build();
            roleAclList.add(roleAcl);
        }
        sysRolePowerMapper.batchInsert(roleAclList);
    }

    public void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after){
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_POWER);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2Str(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2Str(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
}
