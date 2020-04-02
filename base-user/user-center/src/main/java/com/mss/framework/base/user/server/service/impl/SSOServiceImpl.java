package com.mss.framework.base.user.server.service.impl;

import com.mss.framework.base.core.util.DateUtil;
import com.mss.framework.base.core.util.IDUtil;
import com.mss.framework.base.user.server.dao.SSOAccessTokenMapper;
import com.mss.framework.base.user.server.dao.SSOClientDetailMapper;
import com.mss.framework.base.user.server.dao.SSORefreshTokenMapper;
import com.mss.framework.base.user.server.enums.ExpireEnum;
import com.mss.framework.base.user.server.pojo.SSOAccessToken;
import com.mss.framework.base.user.server.pojo.SSOClientDetail;
import com.mss.framework.base.user.server.pojo.SSORefreshToken;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.SSOService;
import com.mss.framework.base.user.server.web.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public String createAccessToken(User user, Long expireIn, String requestIP, SSOClientDetail ssoClientDetail) {
        //过期的时间戳
        long expireTime = DateUtil.nextDaysSecond(ExpireEnum.ACCESS_TOKEN.getTime(), null);

        String accessTokenStr = TokenUtil.accessToken(user, expireIn);
        //保存Access Token
        SSOAccessToken accessToken = ssoAccessTokenMapper.selectByUserIdClientId(user.getId(), ssoClientDetail.getId());

        Date currentTime = new Date();
        //如果存在匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (accessToken == null) {
            accessToken = new SSOAccessToken();
            accessToken.setId(IDUtil.UUIDStr());
            accessToken.setAccessToken(accessTokenStr);
            accessToken.setUserId(user.getId());
            accessToken.setUserName(user.getNickname());
            accessToken.setUserIp(requestIP);
            accessToken.setClientId(ssoClientDetail.getId());
            accessToken.setChannel(ssoClientDetail.getClientName());
            accessToken.setExpiresIn(expireTime);
            accessToken.setCreateUser(user.getId());
            accessToken.setUpdateUser(user.getId());
            accessToken.setCreateTime(currentTime);
            accessToken.setUpdateTime(currentTime);
            ssoAccessTokenMapper.insertSelective(accessToken);
        } else {
            accessToken.setAccessToken(accessTokenStr);
            accessToken.setExpiresIn(expireIn);
            accessToken.setUpdateUser(user.getId());
            accessToken.setUpdateTime(currentTime);
            ssoAccessTokenMapper.updateByPrimaryKeySelective(accessToken);
        }
        return accessTokenStr;
    }

    @Override
    public String createRefreshToken(User user, String accessTokenId) {
        //过期时长
        long expiresIn = DateUtil.dayToMillis(ExpireEnum.REFRESH_TOKEN.getTime());
        //过期的时间戳
        long expiresTime = DateUtil.nextDaysSecond(ExpireEnum.REFRESH_TOKEN.getTime(), null);

        String refreshTokenStr = TokenUtil.refreshToken(user.getId(), expiresIn);
        //3. 保存Refresh Token
        SSORefreshToken refreshToken = ssoRefreshTokenMapper.selectByTokenId(accessTokenId);

        Date currentTime = new Date();
        //如果存在tokenId匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (refreshToken == null) {
            refreshToken = new SSORefreshToken();
            refreshToken.setId(IDUtil.UUIDStr());
            refreshToken.setTokenId(accessTokenId);
            refreshToken.setRefreshToken(refreshTokenStr);
            refreshToken.setExpiresIn(expiresTime);
            refreshToken.setCreateUser(user.getId());
            refreshToken.setUpdateUser(user.getId());
            refreshToken.setCreateTime(currentTime);
            ssoRefreshTokenMapper.insertSelective(refreshToken);
        } else {
            refreshToken.setRefreshToken(refreshTokenStr);
            refreshToken.setExpiresIn(expiresIn);
            refreshToken.setUpdateUser(user.getId());
            refreshToken.setUpdateTime(currentTime);
            ssoRefreshTokenMapper.updateByPrimaryKeySelective(refreshToken);
        }
        return refreshTokenStr;
    }

    @Override
    public List<String> getAllRedirectUri() {
        List<SSOClientDetail> ssoClientDetailList = ssoClientDetailMapper.selectPageList(new SSOClientDetail());
        return ssoClientDetailList.stream().map(SSOClientDetail::getRedirectUrl).collect(Collectors.toList());
    }
}
