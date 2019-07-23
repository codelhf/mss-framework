package com.mss.framework.base.user.server.service.impl;

import com.mss.framework.base.core.token.TokenUtil;
import com.mss.framework.base.core.util.DateUtil;
import com.mss.framework.base.core.util.EncryptUtil;
import com.mss.framework.base.core.util.IDUtil;
import com.mss.framework.base.core.token.UserUtil;
import com.mss.framework.base.user.server.common.Constants;
import com.mss.framework.base.user.server.dao.*;
import com.mss.framework.base.user.server.enums.ExpireEnum;
import com.mss.framework.base.user.server.pojo.*;
import com.mss.framework.base.user.server.service.OAuthService;
import com.mss.framework.base.core.token.TokenUser;
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
    private OAuthAppDetailMapper oAuthAppDetailMapper;
    @Autowired
    private OAuthAppUserMapper oAuthAppUserMapper;
    @Autowired
    private OAuthRefreshTokenMapper oAuthRefreshTokenMapper;
    @Autowired
    private OAuthScopeMapper oAuthScopeMapper;

    @Override
    public OAuthAppDetail register(OAuthAppDetail oAuthAppDetail) {
        //客户端的名称和回调地址不能为空
        if (StringUtils.isAnyBlank(oAuthAppDetail.getAppName(), oAuthAppDetail.getRedirectUri())) {
            return null;
        }
        TokenUser user = UserUtil.getCurrentUser();
        Date currentTime = new Date();
        oAuthAppDetail.setId(IDUtil.UUIDStr());
        oAuthAppDetail.setAppId(IDUtil.UUIDStr());
        oAuthAppDetail.setAppSecret(IDUtil.UUIDStr());
        //此用户为开发者
        oAuthAppDetail.setCreateUser(user.getId());
        oAuthAppDetail.setUpdateUser(user.getId());
        oAuthAppDetail.setCreateTime(currentTime);
        oAuthAppDetail.setUpdateTime(currentTime);
        oAuthAppDetail.setStatus(Constants.StatusEnum.ACTIVATED.getCode());
        int rowCount = oAuthAppDetailMapper.insertSelective(oAuthAppDetail);
        return rowCount == 1 ? oAuthAppDetail : null;
    }

    @Override
    public boolean saveOAuthAppUser(String userId, String appId, String scopeId) {
        OAuthAppDetail clientDetail = oAuthAppDetailMapper.selectByAppId(appId);
        //在授权页面用户自己选择权限类型
        OAuthScope oAuthScope = oAuthScopeMapper.selectByPrimaryKey(scopeId);
        if (clientDetail == null || oAuthScope == null) {
            return false;
        }
        OAuthAppUser oAuthAppUser = oAuthAppUserMapper.selectByExample(userId, clientDetail.getId(), oAuthScope.getId());
        //如果数据库中不存在记录，则插入
        if (oAuthAppUser == null) {
            oAuthAppUser = new OAuthAppUser();
            oAuthAppUser.setId(IDUtil.UUIDStr());
            oAuthAppUser.setUserId(userId);
            oAuthAppUser.setAppId(clientDetail.getId());
            oAuthAppUser.setScopeId(oAuthScope.getId());
            int rowCount = oAuthAppUserMapper.insertSelective(oAuthAppUser);
            return rowCount != 0;
        }
        return true;
    }

    @Override
    public String createAuthorizationCode(String appId, String scope, TokenUser tokenUser) {
        tokenUser.setAppId(appId);
        tokenUser.setScope(scope);
        //生成Authorization Code
        String authorizationCode = TokenUtil.accessToken(tokenUser, null);

        //3. 返回Authorization Code
        return authorizationCode;
    }

    @Override
    public String createAccessToken(TokenUser tokenUser, OAuthAppDetail oAuthAppDetail, String grantType, String scope, Long expireIn) {
        //过期时间戳
        Long expireTime = DateUtil.nextDaysSecond(ExpireEnum.ACCESS_TOKEN.getTime(), null);

        //1. 拼装待加密字符串（username + appId + 当前精确到毫秒的时间戳）
        String str = tokenUser.getUsername() + oAuthAppDetail.getAppId() + String.valueOf(DateUtil.currentTimeMillis());

        //2. SHA1加密
        String accessTokenStr = "1." + EncryptUtil.sha1Hex(str) + "." + expireIn + "." + expireTime;

        //3. 保存Access Token
        OAuthAccessToken accessToken = oAuthAccessTokenMapper.selectByUserIdAppIdScope(tokenUser.getId(), oAuthAppDetail.getAppId(), scope);
        Date current = new Date();
        //如果存在userId + appId + scope匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (accessToken == null) {
            accessToken = new OAuthAccessToken();
            accessToken.setId(IDUtil.UUIDStr());
            accessToken.setAccessToken(accessTokenStr);
            accessToken.setUserId(tokenUser.getId());
            accessToken.setUserName(tokenUser.getUsername());
            accessToken.setAppId(oAuthAppDetail.getId());
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
    public OAuthAppDetail selectById(String id) {
        return oAuthAppDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public OAuthAppDetail selectByAppId(String appId) {
        return oAuthAppDetailMapper.selectByAppId(appId);
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
