package com.mss.framework.base.user.server.service.impl;

import com.mss.framework.base.core.token.TokenUser;
import com.mss.framework.base.core.util.DateUtil;
import com.mss.framework.base.core.util.EncryptUtil;
import com.mss.framework.base.core.util.IDUtil;
import com.mss.framework.base.user.server.dao.SSOAccessTokenMapper;
import com.mss.framework.base.user.server.dao.SSOClientDetailMapper;
import com.mss.framework.base.user.server.dao.SSORefreshTokenMapper;
import com.mss.framework.base.user.server.enums.ExpireEnum;
import com.mss.framework.base.user.server.pojo.SSOAccessToken;
import com.mss.framework.base.user.server.pojo.SSOClientDetail;
import com.mss.framework.base.user.server.pojo.SSORefreshToken;
import com.mss.framework.base.user.server.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 10:15
 */
@Service
public class SSOServiceImpl implements SSOService {

    @Autowired
    private SSOAccessTokenMapper ssoAccessTokenMapper;
    @Autowired
    private SSOClientDetailMapper ssoClientDetailMapper;
    @Autowired
    private SSORefreshTokenMapper ssoRefreshTokenMapper;

    @Override
    public SSOClientDetail selectById(String id) {
        return ssoClientDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public SSOClientDetail selectByRedirectUri(String redirectUri) {
        return ssoClientDetailMapper.selectByRedirectUrl(redirectUri);
    }

    @Override
    public SSOAccessToken selectByAccessId(String id) {
        return ssoAccessTokenMapper.selectByPrimaryKey(id);
    }

    @Override
    public SSOAccessToken selectByAccessToken(String accessToken) {
        return ssoAccessTokenMapper.selectByAccessToken(accessToken);
    }

    @Override
    public SSORefreshToken selectByTokenId(String tokenId) {
        return ssoRefreshTokenMapper.selectByTokenId(tokenId);
    }

    @Override
    public SSORefreshToken selectByRefreshToken(String refreshToken) {
        return ssoRefreshTokenMapper.selectByRefreshToken(refreshToken);
    }

    @Override
    public String createAccessToken(TokenUser tokenUser, Long expireIn, String requestIP, SSOClientDetail ssoClientDetail) {
        //过期的时间戳
        Long expireTime = DateUtil.nextDaysSecond(ExpireEnum.ACCESS_TOKEN.getTime(), null);
        //1. 拼装待加密字符串（username + 渠道CODE + 当前精确到毫秒的时间戳）
        String str = tokenUser.getUsername() + ssoClientDetail.getClientName() + String.valueOf(DateUtil.currentTimeMillis());
        //2. SHA1加密
        String accessTokenStr = "11." + EncryptUtil.sha1Hex(str) + "." + expireIn + "." + expireTime;
        //3. 保存Access Token
        SSOAccessToken accessToken = ssoAccessTokenMapper.selectByUserIdClientId(tokenUser.getId(), ssoClientDetail.getId());
        Date currentTime = new Date();
        //如果存在匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (accessToken == null) {
            accessToken = new SSOAccessToken();
            accessToken.setId(IDUtil.UUIDStr());
            accessToken.setAccessToken(accessTokenStr);
            accessToken.setUserId(tokenUser.getId());
            accessToken.setUserName(tokenUser.getUsername());
            accessToken.setUserIp(requestIP);
            accessToken.setClientId(ssoClientDetail.getId());
            accessToken.setChannel(ssoClientDetail.getClientName());
            accessToken.setExpiresIn(expireIn);
            accessToken.setCreateUser(tokenUser.getId());
            accessToken.setUpdateUser(tokenUser.getId());
            accessToken.setCreateTime(currentTime);
            accessToken.setUpdateTime(currentTime);
            ssoAccessTokenMapper.insertSelective(accessToken);
        } else {
            accessToken.setAccessToken(accessTokenStr);
            accessToken.setExpiresIn(expireIn);
            accessToken.setUpdateUser(tokenUser.getId());
            accessToken.setUpdateTime(currentTime);
            ssoAccessTokenMapper.updateByPrimaryKeySelective(accessToken);
        }
        return accessTokenStr;
    }

    @Override
    public String createRefreshToken(TokenUser tokenUser, SSOAccessToken ssoAccessToken) {
        //过期时间
        Long expiresIn = DateUtil.dayToSecond(ExpireEnum.REFRESH_TOKEN.getTime());
        //过期的时间戳
        Long expiresAt = DateUtil.nextDaysSecond(ExpireEnum.REFRESH_TOKEN.getTime(), null);
        //1. 拼装待加密字符串（username + accessToken + 当前精确到毫秒的时间戳）
        String str = tokenUser.getUsername() + ssoAccessToken.getAccessToken() + String.valueOf(DateUtil.currentTimeMillis());
        //2. SHA1加密
        String refreshTokenStr = "12." + EncryptUtil.sha1Hex(str) + "." + expiresIn + "." + expiresAt;
        //3. 保存Refresh Token
        SSORefreshToken refreshToken = ssoRefreshTokenMapper.selectByTokenId(ssoAccessToken.getId());
        Date current = new Date();
        //如果存在tokenId匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (refreshToken == null) {
            refreshToken = new SSORefreshToken();
            refreshToken.setId(IDUtil.UUIDStr());
            refreshToken.setTokenId(ssoAccessToken.getId());
            refreshToken.setRefreshToken(refreshTokenStr);
            refreshToken.setExpiresIn(expiresIn);
            refreshToken.setCreateUser(tokenUser.getId());
            refreshToken.setUpdateUser(tokenUser.getId());
            refreshToken.setCreateTime(current);
            ssoRefreshTokenMapper.insertSelective(refreshToken);
        } else {
            refreshToken.setRefreshToken(refreshTokenStr);
            refreshToken.setExpiresIn(expiresIn);
            refreshToken.setUpdateUser(tokenUser.getId());
            refreshToken.setUpdateTime(current);
            ssoRefreshTokenMapper.updateByPrimaryKeySelective(refreshToken);
        }
        return refreshTokenStr;
    }
}
