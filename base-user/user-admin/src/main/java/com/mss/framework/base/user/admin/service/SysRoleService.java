package com.mss.framework.base.user.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.admin.common.RequestHolder;
import com.mss.framework.base.user.admin.dao.SysRoleMapper;
import com.mss.framework.base.user.admin.dao.SysRolePowerMapper;
import com.mss.framework.base.user.admin.dao.SysRoleUserMapper;
import com.mss.framework.base.user.admin.dao.SysUserMapper;
import com.mss.framework.base.user.admin.dto.SysRoleDTO;
import com.mss.framework.base.user.admin.excepton.ParamException;
import com.mss.framework.base.user.admin.pojo.SysRole;
import com.mss.framework.base.user.admin.pojo.SysRolePower;
import com.mss.framework.base.user.admin.pojo.SysRoleUser;
import com.mss.framework.base.user.admin.pojo.SysUser;
import com.mss.framework.base.user.admin.util.BeanValidator;
import com.mss.framework.base.user.admin.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysRolePowerMapper sysRolePowerMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogService sysLogService;

    public ServerResponse list(int pageNum, int pageSize, String roleName){
        IPage<SysRole> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("name", roleName);
        wrapper.orderByDesc("update_time");
        IPage<SysRole> sysRoleList = sysRoleMapper.selectPage(page, wrapper);
        return ServerResponse.createBySuccess(sysRoleList);
    }

    public ServerResponse save(SysRoleDTO param){
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())){
            throw new ParamException("角色名称已经存在");
        }
        SysRole role = SysRole.builder()
                .name(param.getName())
                .type(param.getType())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        role.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
        role.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setUpdateTime(new Date());
        int rowCount = sysRoleMapper.insert(role);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("保存角色信息失败");
        }
        sysLogService.saveRoleLog(null, role);
        return ServerResponse.createBySuccess("保存角色信息成功");
    }

    private boolean checkExist(String name, String id){
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("name", name);
        return sysRoleMapper.selectCount(wrapper) > 0;
    }

    public ServerResponse update(SysRoleDTO param){
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())){
            throw new ParamException("角色名称已经存在");
        }
        SysRole before = sysRoleMapper.selectById(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");
        SysRole after = SysRole.builder()
                .id(param.getId())
                .name(param.getName())
                .type(param.getType())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        after.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
        after.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setUpdateTime(new Date());
        int rowCount = sysRoleMapper.updateById(after);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新角色信息失败");
        }
        sysLogService.saveRoleLog(before, after);
        return ServerResponse.createBySuccessMessage("更新角色信息成功");
    }

    public List<SysRole> getRoleListByUserId(String userId){
        QueryWrapper<SysRoleUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<String> roleIdList = sysRoleUserMapper.selectList(wrapper).stream().map(SysRoleUser::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        return sysRoleMapper.selectBatchIds(roleIdList);
    }

    public List<SysRole> getRoleListByAclId(String powerId){
        QueryWrapper<SysRolePower> wrapper = new QueryWrapper<>();
        wrapper.eq("power_id", powerId);
        List<String> roleIdList = sysRolePowerMapper.selectList(wrapper).stream().map(SysRolePower::getRoleId).collect(Collectors.toList());

//        List<String> roleIdList = sysRolePowerMapper.getRoleIdListByAclId(powerId);
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        return sysRoleMapper.selectBatchIds(roleIdList);
    }

    public List<SysUser> getUserListByRoleList(List<SysRole> roleList){
        if (CollectionUtils.isEmpty(roleList)){
            return Lists.newArrayList();
        }
        List<String> roleIdList = roleList.stream().map(SysRole::getId).collect(Collectors.toList());
        QueryWrapper<SysRoleUser> wrapper = new QueryWrapper<>();
        wrapper.in("role_id", roleIdList);
        List<String> userIdList = sysRoleUserMapper.selectList(wrapper).stream().map(SysRoleUser::getUserId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIdList)){
            return Lists.newArrayList();
        }
        return sysUserMapper.selectBatchIds(userIdList);
    }
}
