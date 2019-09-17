package com.mss.framework.base.user.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.core.util.IDUtil;
import com.mss.framework.base.user.admin.common.Constants;
import com.mss.framework.base.user.admin.common.LogType;
import com.mss.framework.base.user.admin.common.RequestHolder;
import com.mss.framework.base.user.admin.dao.SysLogMapper;
import com.mss.framework.base.user.admin.dao.SysRoleUserMapper;
import com.mss.framework.base.user.admin.dao.SysUserMapper;
import com.mss.framework.base.user.admin.pojo.SysLog;
import com.mss.framework.base.user.admin.pojo.SysRoleUser;
import com.mss.framework.base.user.admin.pojo.SysUser;
import com.mss.framework.base.user.admin.util.IpUtil;
import com.mss.framework.base.user.admin.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogMapper sysLogMapper;

    public List<SysUser> getListByRoleId(String roleId){
        QueryWrapper<SysRoleUser> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        List<String> userIdList = sysRoleUserMapper.selectList(wrapper).stream().map(SysRoleUser::getUserId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIdList)){
            return Lists.newArrayList();
        }
        return sysUserMapper.selectBatchIds(userIdList);
    }

    public void changeRoleUser(String roleId, List<String> userIdList){
        QueryWrapper<SysRoleUser> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        List<String> originUserIdList = sysRoleUserMapper.selectList(wrapper).stream().map(SysRoleUser::getUserId).collect(Collectors.toList());
        if (originUserIdList.size() == userIdList.size()){
            Set<String> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<String> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)){
                return;
            }
        }
        updateRoleUsers(roleId, userIdList);
        saveRoleUserLog(roleId, originUserIdList, userIdList);
    }
    @Transactional
    void updateRoleUsers(String roleId, List<String> userIdList){
        QueryWrapper<SysRoleUser> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        //先删除角色所有用户
        sysRoleUserMapper.delete(wrapper);
        if (CollectionUtils.isEmpty(userIdList)){
            return;
        }
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (String userId : userIdList){
            SysRoleUser roleUser = SysRoleUser.builder()
                    .id(IDUtil.UUIDStr())
                    .roleId(roleId)
                    .userId(userId)
                    .updateUser(RequestHolder.getCurrentUser().getUsername())
                    .updateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .updateTime(new Date())
                    .build();
            roleUserList.add(roleUser);
        }
        //在新增选中用户
        sysRoleUserMapper.batchInsert(roleUserList);
    }

    public void saveRoleUserLog(String roleId, List<String> before, List<String> after){
        SysLog sysLog = new SysLog();
        sysLog.setType(LogType.TYPE_ROLE_USER);
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
