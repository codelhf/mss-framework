package com.mss.framework.base.user.server.service.impl;

import com.mss.framework.base.core.common.ResponseCode;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.core.util.IDUtil;
import com.mss.framework.base.user.server.common.Constants;
import com.mss.framework.base.user.server.dao.UserLoginMapper;
import com.mss.framework.base.user.server.dao.UserMapper;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.pojo.UserLogin;
import com.mss.framework.base.user.server.redis.RedisService;
import com.mss.framework.base.user.server.service.LoginService;
import com.mss.framework.base.user.server.util.MD5Util;
import com.mss.framework.base.user.server.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/15 23:34
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Override
    public ServerResponse<String> checkAccount(String account, String loginType) {
        if (StringUtils.isAnyBlank(account, loginType)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    "account or loginType can not empty");
        }
        if (Constants.LoginType.USERNAME.equals(loginType)) {
            int rowCount = userLoginMapper.selectByUsername(account);
            if (rowCount == 0) {
                return ServerResponse.createByErrorMessage("用户名不存在");
            }
            return ServerResponse.createBySuccessMessage("用户名已存在");
        } else if (Constants.LoginType.EMAIL.equals(loginType)) {
            int rowCount = userLoginMapper.selectByEmail(account);
            if (rowCount == 0) {
                return ServerResponse.createByErrorMessage("邮箱不存在");
            }
            return ServerResponse.createBySuccessMessage("邮箱已存在");
        } else if (Constants.LoginType.PHONE.equals(loginType)) {
            int rowCount = userLoginMapper.selectByPhone(account);
            if (rowCount == 0) {
                return ServerResponse.createByErrorMessage("手机号不存在");
            }
            return ServerResponse.createBySuccessMessage("手机号已存在");
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "loginTyp not support");
        }
    }

    @Override
    public ServerResponse<User> login(String account, String md5Password, String vCode, String loginType) {
        ServerResponse response = checkAccount(account, loginType);
        if (!response.isSuccess()) {
            return response;
        }
        if (StringUtils.isAnyBlank(account, md5Password)) {
            return ServerResponse.createByErrorMessage("用户名或密码不能为空");
        }
        String redisVCode = redisService.get("vCoe:" + account);
        if (StringUtils.isBlank(redisVCode)) {
            return ServerResponse.createByErrorMessage("验证码已过期, 请重新获取");
        }
        if (!vCode.equals(redisVCode)) {
            return ServerResponse.createByErrorMessage("验证码不正确");
        }
        String MD5Password = MD5Util.MD5EncodeUtf8(md5Password);
        UserLogin userLogin = null;
        switch (loginType) {
            case Constants.LoginType.USERNAME:
                userLogin = userLoginMapper.loginByUsername(account, MD5Password);
                break;
            case Constants.LoginType.EMAIL:
                userLogin = userLoginMapper.loginByEmail(account, MD5Password);
                break;
            case Constants.LoginType.PHONE:
                userLogin = userLoginMapper.loginByPhone(account, MD5Password);
                break;
            default:
                return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "loginTyp not support");
        }
        if (userLogin == null) {
            return ServerResponse.createByErrorMessage("密码不正确");
        }
        User user = userMapper.selectByPrimaryKey(userLogin.getUserId());
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        return ServerResponse.createBySuccess(user);
    }

    @Override
    @Transactional
    public ServerResponse<String> register(String account, String md5Password, String loginType) {
        ServerResponse response = checkAccount(account, loginType);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage(response.getMsg());
        }
        String MD5Password = MD5Util.MD5EncodeUtf8(md5Password);
        User user = new User();
        UserLogin userLogin = new UserLogin();
        String userId = IDUtil.UUIDStr();
        user.setId(userId);
        userLogin.setId(IDUtil.UUIDStr());
        userLogin.setUserId(userId);
        if (Constants.LoginType.USERNAME.equals(loginType)) {
            user.setNickname(account);
            userLogin.setUsername(account);
        } else if (Constants.LoginType.EMAIL.equals(loginType)) {
            user.setEmail(account);
            userLogin.setEmail(account);
        } else if (Constants.LoginType.PHONE.equals(loginType)) {
            user.setPhone(account);
            userLogin.setPhone(Integer.valueOf(account));
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "loginTyp not support");
        }
        Date currentTime = new Date();
        user.setCreateTime(currentTime);
        user.setUpdateTime(currentTime);
        userLogin.setPassword(MD5Password);
        userLogin.setCreateTime(currentTime);
        userLogin.setUpdateTime(currentTime);
        int rowCount = userMapper.insertSelective(user);
        if (rowCount == 0) {
            throw new RuntimeException("新增用户失败");
        }
        rowCount = userLoginMapper.insertSelective(userLogin);
        if (rowCount == 0) {
            throw new RuntimeException("保存用户登录信息失败");
        }
        return ServerResponse.createBySuccess();
    }
}
