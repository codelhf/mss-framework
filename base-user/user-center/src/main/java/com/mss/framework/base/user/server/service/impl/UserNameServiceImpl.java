package com.mss.framework.base.user.server.service.impl;

import com.mss.framework.base.core.common.ResponseCode;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.core.util.IDUtil;
import com.mss.framework.base.user.server.dao.UserLoginMapper;
import com.mss.framework.base.user.server.dao.UserMapper;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.pojo.UserLogin;
import com.mss.framework.base.user.server.redis.RedisService;
import com.mss.framework.base.user.server.service.UserNameService;
import com.mss.framework.base.user.server.util.MD5Util;
import com.mss.framework.base.user.server.web.validate.code.VCodeConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/6/6 12:04
 */
@Service
public class UserNameServiceImpl implements UserNameService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserLoginMapper userLoginMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<String> checkName(String username) {
        if (StringUtils.isBlank(username)) {
            return ServerResponse.createByErrorMessage("用户名不能为空");
        }
        UserLogin userLogin = userLoginMapper.selectByUsername(username);
        if (userLogin == null) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        return ServerResponse.createBySuccessMessage("用户名已存在");
    }

    @Transactional
    @Override
    public ServerResponse<String> register(String username, String md5Password, String vCode) {
        if (StringUtils.isAnyBlank(username, md5Password, vCode)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        ServerResponse response = this.checkName(username);
        if (response.isSuccess()) {
            return response;
        }
        String redisVCode = redisService.get(VCodeConstants.VCODE_PREFIX + username);
        if (StringUtils.isBlank(redisVCode)) {
            return ServerResponse.createByErrorMessage("验证码已过期");
        }
        if (!StringUtils.equals(redisVCode, vCode)) {
            return ServerResponse.createByErrorMessage("验证码不正确,请输入正确的验证码");
        }
        Date currentDateTime = new Date();
        String userId = IDUtil.UUIDStr();
        User user = new User();
        user.setId(userId);
        user.setNickname(username);
        user.setCreateTime(currentDateTime);
        user.setUpdateTime(currentDateTime);
        int rowCount = userMapper.insertSelective(user);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败, 请重新注册");
        }
        String MD5Password = MD5Util.MD5EncodeUtf8(md5Password);
        UserLogin userLogin = new UserLogin();
        userLogin.setId(IDUtil.UUIDStr());
        userLogin.setUserId(userId);
        userLogin.setUsername(username);
        userLogin.setPassword(MD5Password);
        userLogin.setCreateTime(currentDateTime);
        userLogin.setUpdateTime(currentDateTime);
        rowCount = userLoginMapper.insertSelective(userLogin);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败, 请重新注册");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<User> loginPassword(String username, String md5Password) {
        if (StringUtils.isAnyBlank(username, md5Password)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        ServerResponse response = this.checkName(username);
        if (response.isSuccess()) {
            return response;
        }
        String MD5Password = MD5Util.MD5EncodeUtf8(md5Password);
        UserLogin userLogin = userLoginMapper.loginByUsername(username, MD5Password);
        if (userLogin == null) {
            return ServerResponse.createByErrorMessage("登录密码不正确");
        }
        User user = userMapper.selectByPrimaryKey(userLogin.getUserId());
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        return ServerResponse.createBySuccess(user);
    }
}
