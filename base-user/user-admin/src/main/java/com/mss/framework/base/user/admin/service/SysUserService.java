package com.mss.framework.base.user.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.core.util.IDUtil;
import com.mss.framework.base.user.admin.common.RequestHolder;
import com.mss.framework.base.user.admin.dao.SysUserMapper;
import com.mss.framework.base.user.admin.dto.SysUserDTO;
import com.mss.framework.base.user.admin.excepton.ParamException;
import com.mss.framework.base.user.admin.pojo.SysUser;
import com.mss.framework.base.user.admin.util.BeanValidator;
import com.mss.framework.base.user.admin.util.IpUtil;
import com.mss.framework.base.user.admin.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogService sysLogService;

    public ServerResponse list(int pageNum, int pageSize,
                               String username, String phone,
                               String email, String deptId) {
        IPage<SysUser> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            wrapper.like("username", username);
        }
        if (StringUtils.isNotBlank(phone)) {
            wrapper.like("phone", phone);
        }
        if (StringUtils.isNotBlank(email)) {
            wrapper.like("email", email);
        }
        if (StringUtils.isNotBlank(deptId)) {
            wrapper.eq("dept_id", deptId);
        }
        wrapper.orderByDesc("update_time");
        IPage<SysUser> sysUserList = sysUserMapper.selectPage(page, wrapper);
        return ServerResponse.createBySuccess(sysUserList);
    }

    public ServerResponse<String> save(SysUserDTO param) {
        BeanValidator.check(param);
        if (checkTelephoneExist(param.getPhone(), param.getId())) {
            return ServerResponse.createByErrorMessage("电话已被占用");
        }
        if (checkMailExist(param.getEmail(), param.getId())) {
            return ServerResponse.createByErrorMessage("邮箱已被占用");
        }
//        String password = PasswordUtil.randomPassword();
        String password = "123456";
        String encryptedPassword = MD5Util.encrypt(password);
        SysUser user = SysUser.builder()
                .id(IDUtil.UUIDStr())
                .username(param.getUsername())
                .phone(param.getPhone())
                .email(param.getEmail())
                .password(encryptedPassword)
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();

        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        user.setOperateTime(new Date());

        // todo : sendEmail
        int rowCount = sysUserMapper.insert(user);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增用户失败");
        }
        sysLogService.saveUserLog(null, user);
        return ServerResponse.createBySuccessMessage("新增用户成功");
    }

    public ServerResponse<String> update(SysUserDTO param) {
        BeanValidator.check(param);
        if (checkTelephoneExist(param.getPhone(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if (checkMailExist(param.getEmail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }
        SysUser before = sysUserMapper.selectById(param.getId());
        Preconditions.checkNotNull(before, "更新用户不存在");
        SysUser after = SysUser.builder()
                .id(param.getId())
                .username(param.getUsername())
                .phone(param.getPhone())
                .email(param.getEmail())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();

        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        int rowCount = sysUserMapper.updateById(after);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新用户信息失败");
        }
        sysLogService.saveUserLog(before, after);
        return ServerResponse.createBySuccessMessage("更新用户信息成功");
    }

    private boolean checkTelephoneExist(String telephone, String userId) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", telephone);
        wrapper.eq("user_id", userId);
        return sysUserMapper.selectCount(wrapper) > 0;
    }

    private boolean checkMailExist(String mail, String userId) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("email", mail);
        wrapper.eq("user_id", userId);
        return sysUserMapper.selectCount(wrapper) > 0;
    }
}
