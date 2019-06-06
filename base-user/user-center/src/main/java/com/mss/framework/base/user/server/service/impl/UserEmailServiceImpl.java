package com.mss.framework.base.user.server.service.impl;

import com.mss.framework.base.core.common.ResponseCode;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.core.util.IDUtil;
import com.mss.framework.base.user.server.dao.UserLoginMapper;
import com.mss.framework.base.user.server.dao.UserMapper;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.pojo.UserLogin;
import com.mss.framework.base.user.server.redis.RedisService;
import com.mss.framework.base.user.server.service.UserEmailService;
import com.mss.framework.base.user.server.util.MD5Util;
import com.mss.framework.base.user.server.util.RegexUtil;
import com.mss.framework.base.user.server.web.validate.code.VCodeConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/6/6 11:15
 */
@Service
public class UserEmailServiceImpl implements UserEmailService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserLoginMapper userLoginMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<String> checkEmail(String email) {
        if (!RegexUtil.isEmail(email)) {
            return ServerResponse.createByErrorMessage("邮箱格式不正确");
        }
        UserLogin userLogin = userLoginMapper.selectByEmail(email);
        if (userLogin == null) {
            return ServerResponse.createByErrorMessage("邮箱不存在");
        }
        return ServerResponse.createBySuccessMessage("邮箱已存在");
    }

    @Transactional
    @Override
    public ServerResponse<String> register(String email, String md5Password, String vCode) {
        if (StringUtils.isAnyBlank(email, md5Password, vCode)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        ServerResponse response = this.checkEmail(email);
        if (response.isSuccess()) {
            return response;
        }
        String redisVCode = redisService.get(VCodeConstants.VCODE_PREFIX + email);
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
        user.setEmail(email);
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
        userLogin.setEmail(email);
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
    public ServerResponse<User> loginPassword(String email, String md5Password) {
        if (StringUtils.isAnyBlank(email, md5Password)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        ServerResponse response = this.checkEmail(email);
        if (!response.isSuccess()) {
            return response;
        }
        String MD5Password = MD5Util.MD5EncodeUtf8(md5Password);
        UserLogin userLogin = userLoginMapper.loginByEmail(email, MD5Password);
        if (userLogin == null) {
            return ServerResponse.createByErrorMessage("登录密码不正确");
        }
        User user = userMapper.selectByPrimaryKey(userLogin.getUserId());
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<User> loginVCode(String email, String vCode) {
        if (StringUtils.isAnyBlank(email, vCode)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        ServerResponse response = this.checkEmail(email);
        if (!response.isSuccess()) {
            return response;
        }
        String redisVCode = redisService.get(VCodeConstants.VCODE_PREFIX + email);
        if (StringUtils.isBlank(redisVCode)) {
            return ServerResponse.createByErrorMessage("验证码已过期");
        }
        if (!StringUtils.equals(redisVCode, vCode)) {
            return ServerResponse.createByErrorMessage("验证码不正确,请输入正确的验证码");
        }
        UserLogin userLogin = userLoginMapper.selectByEmail(email);
        if (userLogin == null) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        User user = userMapper.selectByPrimaryKey(userLogin.getUserId());
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        return ServerResponse.createBySuccess(user);
    }
}
