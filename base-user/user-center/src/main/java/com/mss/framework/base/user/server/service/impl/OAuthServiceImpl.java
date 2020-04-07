package com.mss.framework.base.user.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.core.token.TokenUser;
import com.mss.framework.base.core.token.UserUtil;
import com.mss.framework.base.core.util.DateUtil;
import com.mss.framework.base.core.util.IDUtil;
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
    private OAuthScopeMapper oAuthScopeMapper;
    @Autowired
    private OAuthRefreshTokenMapper oAuthRefreshTokenMapper;

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
    public OAuthClientDetail selectByClientId(String clientId) {
        return oAuthClientDetailMapper.selectByClientId(clientId);
    }

    @Override
    public boolean saveOAuthClientUser(String userId, String clientId, String scopeId) {
        OAuthClientDetail clientDetail = oAuthClientDetailMapper.selectByClientId(clientId);
        //在授权页面用户自己选择权限类型
        OAuthScope oAuthScope = oAuthScopeMapper.selectByPrimaryKey(scopeId);
        if (clientDetail == null || oAuthScope == null) {
            return false;
        }
        OAuthClientUser oAuthClientUser = oAuthClientUserMapper.selectByExample(userId, clientId, scopeId);
        //如果数据库中不存在记录，则插入
        if (oAuthClientUser == null) {
            oAuthClientUser = new OAuthClientUser();
            oAuthClientUser.setId(IDUtil.UUIDStr());
            oAuthClientUser.setUserId(userId);
            oAuthClientUser.setClientId(clientId);
            oAuthClientUser.setScopeId(scopeId);
            int rowCount = oAuthClientUserMapper.insertSelective(oAuthClientUser);
            return rowCount == 1;
        }
        return true;
    }

    @Override
    public String createAuthorizationCode(String clientId, String scope, String redirectUri) {
        //获取用户信息
        TokenUser tokenUser = UserUtil.getCurrentUser();
        tokenUser.setId("1");
        tokenUser.setUsername("liuhf");
        tokenUser.setClientId(clientId);
        tokenUser.setScope(scope);
        //生成Authorization Code
        return TokenUtil.sign(JSON.toJSONString(tokenUser), ExpireEnum.AUTHORIZATION_CODE.getTime(), ExpireEnum.AUTHORIZATION_CODE.getTimeUnit());
    }

    @Override
    public String createAccessToken(User user, String clientId, String grantType, String scope, Long expireIn) {

        //过期时间戳
        long expireTime = DateUtil.nextDaysSecond(ExpireEnum.ACCESS_TOKEN.getTime(), null);

        String accessTokenStr = TokenUtil.accessToken(user, expireIn);

        //3. 保存Access Token
        OAuthAccessToken oAuthAccessToken = oAuthAccessTokenMapper.selectByUserIdClientIdScope(user.getId(), clientId, scope);

        Date currentTime = new Date();
        //如果存在userId + clientId + scope匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (oAuthAccessToken == null) {
            oAuthAccessToken = new OAuthAccessToken();
            oAuthAccessToken.setId(IDUtil.UUIDStr());
            oAuthAccessToken.setAccessToken(accessTokenStr);
            oAuthAccessToken.setUserId(user.getId());
            oAuthAccessToken.setUserName(user.getNickname());
            oAuthAccessToken.setClientId(clientId);
            oAuthAccessToken.setScope(scope);
            oAuthAccessToken.setExpiresIn(expireTime);
            oAuthAccessToken.setGrantType(grantType);
            oAuthAccessToken.setCreateUser(user.getId());
            oAuthAccessToken.setUpdateUser(user.getId());
            oAuthAccessToken.setCreateTime(currentTime);
            oAuthAccessToken.setUpdateTime(currentTime);
            oAuthAccessTokenMapper.insert(oAuthAccessToken);
        } else {
            oAuthAccessToken.setAccessToken(accessTokenStr);
            oAuthAccessToken.setExpiresIn(expireTime);
            oAuthAccessToken.setUpdateUser(user.getId());
            oAuthAccessToken.setUpdateTime(currentTime);
            oAuthAccessTokenMapper.updateByPrimaryKeySelective(oAuthAccessToken);
        }
        return accessTokenStr;
    }

    @Override
    public OAuthAccessToken selectByAccessToken(String accessToken) {
        return oAuthAccessTokenMapper.selectByAccessToken(accessToken);
    }

    @Override
    public String createRefreshToken(User user, String accessTokenId) {
        //过期时长
        long expireIn = DateUtil.dayToSecond(ExpireEnum.REFRESH_TOKEN.getTime());
        //过期时间戳
        long expireTime = DateUtil.nextDaysSecond(ExpireEnum.REFRESH_TOKEN.getTime(), null);

        String refreshTokenStr = TokenUtil.refreshToken(user.getId(), expireIn);

        //3. 保存Refresh Token
        OAuthRefreshToken refreshToken = oAuthRefreshTokenMapper.selectByTokenId(accessTokenId);

        Date currentTime = new Date();
        //如果存在tokenId匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (refreshToken == null) {
            refreshToken = new OAuthRefreshToken();
            refreshToken.setId(IDUtil.UUIDStr());
            refreshToken.setTokenId(accessTokenId);
            refreshToken.setRefreshToken(refreshTokenStr);
            refreshToken.setExpiresIn(expireTime);
            refreshToken.setCreateUser(user.getId());
            refreshToken.setUpdateUser(user.getId());
            refreshToken.setCreateTime(currentTime);
            refreshToken.setUpdateTime(currentTime);
            oAuthRefreshTokenMapper.insert(refreshToken);
        } else {
            refreshToken.setRefreshToken(refreshTokenStr);
            refreshToken.setExpiresIn(expireTime);
            refreshToken.setUpdateUser(user.getId());
            refreshToken.setUpdateTime(currentTime);
            oAuthRefreshTokenMapper.updateByPrimaryKeySelective(refreshToken);
        }
        return refreshTokenStr;
    }

    @Override
    public OAuthAccessToken selectByAccessId(String id) {
        return oAuthAccessTokenMapper.selectByPrimaryKey(id);
    }

    @Override
    public OAuthClientDetail selectById(String id) {
        return oAuthClientDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public OAuthRefreshToken selectByRefreshToken(String refreshToken) {
        return oAuthRefreshTokenMapper.selectByRefreshToken(refreshToken);
    }
}
