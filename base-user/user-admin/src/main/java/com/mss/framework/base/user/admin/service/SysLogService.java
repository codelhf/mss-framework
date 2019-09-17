package com.mss.framework.base.user.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.admin.common.LogType;
import com.mss.framework.base.user.admin.common.Constants;
import com.mss.framework.base.user.admin.common.RequestHolder;
import com.mss.framework.base.user.admin.dao.*;
import com.mss.framework.base.user.admin.dto.SysLogDTO;
import com.mss.framework.base.user.admin.excepton.ParamException;
import com.mss.framework.base.user.admin.pojo.*;
import com.mss.framework.base.user.admin.util.IpUtil;
import com.mss.framework.base.user.admin.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysFuncMapper sysFuncMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleFuncService sysRoleFuncService;
    @Autowired
    private SysRoleUserService sysRoleUserService;

    public ServerResponse searchPageList(int pageNum, int pageSize, SysLogDTO param) {
        IPage<SysLog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysLog> wrapper = new QueryWrapper<>();
        if (param.getType() != null) {
            wrapper.eq("type", param.getType());
        }
        if (StringUtils.isNotBlank(param.getBeforeSeq())) {
            wrapper.like("old_value", param.getBeforeSeq());
        }
        if (StringUtils.isNotBlank(param.getAfterSeq())) {
            wrapper.like("new_value", param.getAfterSeq());
        }
        if (StringUtils.isNotBlank(param.getOperator())) {
            wrapper.like("update_user", param.getOperator());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (StringUtils.isNotBlank(param.getFromTime())) {
                wrapper.ge("update_time", dateFormat.parse(param.getFromTime()));
            }
            if (StringUtils.isNotBlank(param.getToTime())) {
                wrapper.lt("update_time", dateFormat.parse(param.getToTime()));
            }
        } catch (ParseException e) {
            throw new ParamException("传入的日期格式有问题， 正确格式为：yyyy-MM-dd HH:mm:ss");
        }
        wrapper.orderByDesc("update_time");
        IPage<SysLog> sysLogIPage = sysLogMapper.selectPage(page, wrapper);
        return ServerResponse.createBySuccess(sysLogIPage);
    }

    public void recover(String id) {
        SysLog sysLog = sysLogMapper.selectById(id);
        Preconditions.checkNotNull(sysLog, "待还原的记录不存在");
        switch (sysLog.getType()) {
            case LogType.TYPE_DEPT:
                SysDept beforeDept = sysDeptMapper.selectById(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeDept, "待还原的部门已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                SysDept afterDept = JsonMapper.str2obj(sysLog.getOldValue(), new TypeReference<SysDept>() {
                });
                afterDept.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
                afterDept.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterDept.setUpdateTime(new Date());
                sysDeptMapper.updateById(afterDept);
                saveDeptLog(beforeDept, afterDept);
                break;
            case LogType.TYPE_USER:
                SysUser beforeUser = sysUserMapper.selectById(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeUser, "待还原的用户已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                SysUser afterUser = JsonMapper.str2obj(sysLog.getOldValue(), new TypeReference<SysUser>() {
                });
                afterUser.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterUser.setOperateTime(new Date());
                sysUserMapper.updateById(afterUser);
                saveUserLog(beforeUser, afterUser);
                break;
            case LogType.TYPE_ROLE:
                SysRole beforeRole = sysRoleMapper.selectById(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeRole, "待还原的角色已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                SysRole afterRole = JsonMapper.str2obj(sysLog.getOldValue(), new TypeReference<SysRole>() {
                });
                afterRole.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
                afterRole.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterRole.setUpdateTime(new Date());
                sysRoleMapper.updateById(afterRole);
                saveRoleLog(beforeRole, afterRole);
                break;
            case LogType.TYPE_FUNC:
                SysFunc beforeFunc = sysFuncMapper.selectById(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeFunc, "待还原的权限点已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                SysFunc afterFunc = JsonMapper.str2obj(sysLog.getOldValue(), new TypeReference<SysFunc>() {
                });
                afterFunc.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
                afterFunc.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterFunc.setUpdateTime(new Date());
                sysFuncMapper.updateById(afterFunc);
                saveFuncLog(beforeFunc, afterFunc);
                break;
            case LogType.TYPE_ROLE_FUNC:
                SysRole funcRole = sysRoleMapper.selectById(sysLog.getTargetId());
                Preconditions.checkNotNull(funcRole, "待还原的角色已经不存在了");
                sysRoleFuncService.changeRoleFunc(sysLog.getTargetId(), JsonMapper.str2obj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;
            case LogType.TYPE_ROLE_USER:
                SysRole userRole = sysRoleMapper.selectById(sysLog.getTargetId());
                Preconditions.checkNotNull(userRole, "待还原的角色已经不存在了");
                sysRoleUserService.changeRoleUser(sysLog.getTargetId(), JsonMapper.str2obj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;
            default:
        }
    }


    public void saveDeptLog(SysDept before, SysDept after) {
        SysLog sysLog = new SysLog();
        sysLog.setType(LogType.TYPE_DEPT);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2Str(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2Str(after));
        sysLog.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
        sysLog.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setUpdateTime(new Date());
        sysLog.setStatus(Constants.LogStatusEnum.RECOVER.getCode());
        sysLogMapper.insert(sysLog);
    }

    public void saveUserLog(SysUser before, SysUser after) {
        SysLog sysLog = new SysLog();
        sysLog.setType(LogType.TYPE_USER);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2Str(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2Str(after));
        sysLog.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
        sysLog.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setUpdateTime(new Date());
        sysLog.setStatus(Constants.LogStatusEnum.RECOVER.getCode());
        sysLogMapper.insert(sysLog);
    }

    public void saveRoleLog(SysRole before, SysRole after) {
        SysLog sysLog = new SysLog();
        sysLog.setType(LogType.TYPE_ROLE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2Str(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2Str(after));
        sysLog.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
        sysLog.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setUpdateTime(new Date());
        sysLog.setStatus(Constants.LogStatusEnum.RECOVER.getCode());
        sysLogMapper.insert(sysLog);
    }

    public void saveFuncLog(SysFunc before, SysFunc after) {
        SysLog sysLog = new SysLog();
        sysLog.setType(LogType.TYPE_FUNC);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2Str(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2Str(after));
        sysLog.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
        sysLog.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setUpdateTime(new Date());
        sysLog.setStatus(Constants.LogStatusEnum.RECOVER.getCode());
        sysLogMapper.insert(sysLog);
    }
}
