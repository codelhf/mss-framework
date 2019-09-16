package com.mss.framework.base.user.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.admin.dao.SysUserMapper;
import com.mss.framework.base.user.admin.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/9/16
 */
@Service
public class UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    public ServerResponse<SysUser> login(String username, String password) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Integer count = sysUserMapper.selectCount(wrapper);
        if (count == 0) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        wrapper.eq("password", password);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        if (sysUser == null) {
            return ServerResponse.createByErrorMessage("密码不正确");
        }
        return ServerResponse.createBySuccess(sysUser);
    }
}
