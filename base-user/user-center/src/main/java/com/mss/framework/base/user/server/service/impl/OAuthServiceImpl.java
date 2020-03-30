package com.mss.framework.base.user.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.core.token.TokenUser;
import com.mss.framework.base.core.util.DateUtil;
import com.mss.framework.base.core.util.EncryptUtil;
import com.mss.framework.base.core.util.IDUtil;
import com.mss.framework.base.core.token.UserUtil;
import com.mss.framework.base.user.server.common.Constants;
import com.mss.framework.base.user.server.dao.*;
import com.mss.framework.base.user.server.enums.ExpireEnum;
import com.mss.framework.base.user.server.pojo.*;
import com.mss.framework.base.user.server.service.OAuthService;
import com.mss.framework.base.user.server.web.token.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 20:26
 */
@Service
public class OAuthServiceImpl implements OAuthService {

    @Autowired
    private OAuthAccessTokenMapper oAuthAccessTokenMapper;
    @Autowired
    private OAuthClientDetailMapper oAuthClientDetailMapper;
    @Autowired
    private OAuthClientUserMapper oAuthClientUserMapper;
    @Autowired
    private OAuthRefreshTokenMapper oAuthRefreshTokenMapper;
    @Autowired
    private OAuthScopeMapper oAuthScopeMapper;

    @Override
    public OAuthClientDetail register(OAuthClientDetail oAuthClientDetail) {
        //客户端的名称和回调地址不能为空
        if (StringUtils.isAnyBlank(oAuthClientDetail.getClientName(), oAuthClientDetail.getRedirectUri())) {
            return null;
        }
        TokenUser user = UserUtil.getCurrentUser();
        Date currentTime = new Date();
        oAuthClientDetail.setId(IDUtil.UUIDStr());
        oAuthClientDetail.setClientId(IDUtil.UUIDStr());
        oAuthClientDetail.setClientSecret(IDUtil.UUIDStr());
        //此用户为开发者
        oAuthClientDetail.setCreateUser(user.getId());
        oAuthClientDetail.setUpdateUser(user.getId());
        oAuthClientDetail.setCreateTime(currentTime);
        oAuthClientDetail.setUpdateTime(currentTime);
        oAuthClientDetail.setStatus(Constants.StatusEnum.ACTIVATED.getCode());
        int rowCount = oAuthClientDetailMapper.insertSelective(oAuthClientDetail);
        return rowCount == 1 ? oAuthClientDetail : null;
    }

    @Override
    public boolean saveOAuthClientUser(String userId, String clientId, String scopeId) {
        OAuthClientDetail clientDetail = oAuthClientDetailMapper.selectByClientId(clientId);
        //在授权页面用户自己选择权限类型
        OAuthScope oAuthScope = oAuthScopeMapper.selectByPrimaryKey(scopeId);
        if (clientDetail == null || oAuthScope == null) {
            return false;
        }
        OAuthClientUser oAuthClientUser = oAuthClientUserMapper.selectByExample(userId, clientDetail.getId(), oAuthScope.getId());
        //如果数据库中不存在记录，则插入
        if (oAuthClientUser == null) {
            oAuthClientUser = new OAuthClientUser();
            oAuthClientUser.setId(IDUtil.UUIDStr());
            oAuthClientUser.setUserId(userId);
            oAuthClientUser.setClientId(clientDetail.getId());
            oAuthClientUser.setScopeId(oAuthScope.getId());
            int rowCount = oAuthClientUserMapper.insertSelective(oAuthClientUser);
            return rowCount != 0;
        }
        return true;
    }

    @Override
    public String createAuthorizationCode(String clientId, String scope, TokenUser tokenUser) {
        if (tokenUser == null) {
            tokenUser = new TokenUser();
        }
        tokenUser.setId("1");
        tokenUser.setUsername("liuhf");
        tokenUser.setClientId(clientId);
        tokenUser.setScope(scope);
        //生成Authorization Code
        return TokenUtil.sign(JSON.toJSONString(tokenUser), ExpireEnum.AUTHORIZATION_CODE.getTime(), ExpireEnum.AUTHORIZATION_CODE.getTimeUnit());
    }

    @Override
    public String createAccessToken(TokenUser tokenUser, OAuthClientDetail oAuthClientDetail, String grantType, String scope, Long expireIn) {
        //过期时间戳
        Long expireTime = DateUtil.nextDaysSecond(ExpireEnum.ACCESS_TOKEN.getTime(), null);

        //1. 拼装待加密字符串（username + clientId + 当前精确到毫秒的时间戳）
        String str = tokenUser.getUsername() + oAuthClientDetail.getClientId() + String.valueOf(DateUtil.currentTimeMillis());

        //2. SHA1加密
        String accessTokenStr = "1." + EncryptUtil.sha1Hex(str) + "." + expireIn + "." + expireTime;

        //3. 保存Access Token
        OAuthAccessToken accessToken = oAuthAccessTokenMapper.selectByUserIdClientIdScope(tokenUser.getId(), oAuthClientDetail.getClientId(), scope);
        Date current = new Date();
        //如果存在userId + clientId + scope匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (accessToken == null) {
            accessToken = new OAuthAccessToken();
            accessToken.setId(IDUtil.UUIDStr());
            accessToken.setAccessToken(accessTokenStr);
            accessToken.setUserId(tokenUser.getId());
            accessToken.setUserName(tokenUser.getUsername());
            accessToken.setClientId(oAuthClientDetail.getClientId());
            accessToken.setScope(scope);
            accessToken.setExpiresIn(expireTime);
            accessToken.setGrantType(grantType);
            accessToken.setCreateUser(tokenUser.getId());
            accessToken.setUpdateUser(tokenUser.getId());
            accessToken.setCreateTime(current);
            accessToken.setUpdateTime(current);
            oAuthAccessTokenMapper.insert(accessToken);
        } else {
            accessToken.setAccessToken(accessTokenStr);
            accessToken.setExpiresIn(expireTime);
            accessToken.setUpdateUser(tokenUser.getId());
            accessToken.setUpdateTime(current);
            oAuthAccessTokenMapper.updateByPrimaryKeySelective(accessToken);
        }
        return accessTokenStr;
    }

    @Override
    public String createRefreshToken(TokenUser tokenUser, OAuthAccessToken oAuthAccessToken) {
        //过期时间
        Long expireIn = DateUtil.dayToSecond(ExpireEnum.REFRESH_TOKEN.getTime());
        //过期时间戳
        Long expireTime = DateUtil.nextDaysSecond(ExpireEnum.REFRESH_TOKEN.getTime(), null);

        //1. 拼装待加密字符串（username + accessToken + 当前精确到毫秒的时间戳）
        String str = tokenUser.getUsername() + oAuthAccessToken.getAccessToken() + String.valueOf(DateUtil.currentTimeMillis());

        //2. SHA1加密
        String refreshTokenStr = "2." + EncryptUtil.sha1Hex(str) + "." + expireIn + "." + expireTime;

        //3. 保存Refresh Token
        OAuthRefreshToken refreshToken = oAuthRefreshTokenMapper.selectByTokenId(oAuthAccessToken.getId());

        Date current = new Date();
        //如果存在tokenId匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (refreshToken == null) {
            refreshToken = new OAuthRefreshToken();
            refreshToken.setId(IDUtil.UUIDStr());
            refreshToken.setTokenId(oAuthAccessToken.getId());
            refreshToken.setRefreshToken(refreshTokenStr);
            refreshToken.setExpiresIn(expireTime);
            refreshToken.setCreateUser(tokenUser.getId());
            refreshToken.setUpdateUser(tokenUser.getId());
            refreshToken.setCreateTime(current);
            refreshToken.setUpdateTime(current);
            oAuthRefreshTokenMapper.insert(refreshToken);
        } else {
            refreshToken.setRefreshToken(refreshTokenStr);
            refreshToken.setExpiresIn(expireTime);
            refreshToken.setUpdateUser(tokenUser.getId());
            refreshToken.setUpdateTime(current);
            oAuthRefreshTokenMapper.updateByPrimaryKeySelective(refreshToken);
        }
        return refreshTokenStr;
    }

    @Override
    public OAuthClientDetail selectById(String id) {
        return oAuthClientDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public OAuthClientDetail selectByClientId(String clientId) {
        return oAuthClientDetailMapper.selectByClientId(clientId);
    }

    @Override
    public OAuthAccessToken selectByAccessToken(String accessToken) {
        return oAuthAccessTokenMapper.selectByAccessToken(accessToken);
    }

    @Override
    public OAuthAccessToken selectByAccessId(String id) {
        return oAuthAccessTokenMapper.selectByPrimaryKey(id);
    }

    @Override
    public OAuthRefreshToken selectByRefreshToken(String refreshToken) {
        return oAuthRefreshTokenMapper.selectByRefreshToken(refreshToken);
    }
}
