package com.mss.framework.base.user.server.service.impl;

import com.mss.framework.base.user.server.common.Constants;
import com.mss.framework.base.server.user.dao.*;
import com.mss.framework.base.user.server.dao.*;
import com.mss.framework.base.user.server.enums.ExpireEnum;
import com.mss.framework.base.server.user.pojo.*;
import com.mss.framework.base.user.server.pojo.*;
import com.mss.framework.base.user.server.service.IOAuthService;
import com.mss.framework.base.user.server.service.IRedisService;
import com.mss.framework.base.user.server.util.DateUtil;
import com.mss.framework.base.user.server.util.EncryptUtil;
import com.mss.framework.base.user.server.util.IDUtil;
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
public class OAuthServiceImpl implements IOAuthService {

    @Autowired
    private IRedisService iRedisService;

    @Autowired
    private OAuthAccessTokenMapper oAuthAccessTokenMapper;
    @Autowired
    private OAuthRefreshTokenMapper oAuthRefreshTokenMapper;
    @Autowired
    private OAuthScopeMapper oAuthScopeMapper;
    @Autowired
    private OAuthClientDetailMapper oAuthClientDetailMapper;
    @Autowired
    private OAuthClientUserMapper oAuthClientUserMapper;


    @Override
    public boolean register(OAuthClientDetail oAuthClientDetail) {
        //客户端的名称和回调地址不能为空
        if (StringUtils.isNoneBlank(oAuthClientDetail.getClientName(), oAuthClientDetail.getRedirectUri())) {
            //生成24位随机的clientId
            String clientId = EncryptUtil.getRandomStr1(24);
            OAuthClientDetail clientDetail = oAuthClientDetailMapper.selectByClientId(clientId);
            //生成的clientId必须是唯一的
            for (int i = 0; i < 10; i++) {
                if (clientDetail == null) {
                    break;
                } else {
                    clientId = EncryptUtil.getRandomStr1(24);
                    clientDetail = oAuthClientDetailMapper.selectByClientId(clientId);
                }
            }
            //生成32位随机的clientSecret
            String clientSecret = EncryptUtil.getRandomStr1(32);

            User user = iRedisService.get(Constants.SESSION_USER);
            Date current = new Date();
            oAuthClientDetail.setId(IDUtil.UUIDStr());
            oAuthClientDetail.setClientId(clientId);
            oAuthClientDetail.setClientSecret(clientSecret);
            oAuthClientDetail.setCreateUser(user.getId());
            oAuthClientDetail.setUpdateUser(user.getId());
            oAuthClientDetail.setCreateTime(current);
            oAuthClientDetail.setUpdateTime(current);
            oAuthClientDetail.setStatus(1);
            int rowCount = oAuthClientDetailMapper.insertSelective(oAuthClientDetail);
            return rowCount != 0;
        }
        return false;
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

    @Override
    public boolean saveOAuthClientUser(String userId, String clientIdStr, String scopeName) {
        OAuthClientDetail clientDetail = oAuthClientDetailMapper.selectByClientId(clientIdStr);
        OAuthScope oAuthScope = oAuthScopeMapper.selectByScopeName(scopeName);

        if (clientDetail == null || oAuthScope == null) {
            return false;
        }
        OAuthClientUser clientUser = oAuthClientUserMapper.selectByExample(userId, clientDetail.getId(), oAuthScope.getId());
        //如果数据库中不存在记录，则插入
        if (clientUser == null) {
            clientUser = new OAuthClientUser();
            clientUser.setId(IDUtil.UUIDStr());
            clientUser.setUserId(userId);
            clientUser.setClientId(clientDetail.getId());
            clientUser.setScopeId(oAuthScope.getId());
            int rowCount = oAuthClientUserMapper.insertSelective(clientUser);
            return rowCount != 0;
        }
        return true;
    }

    @Override
    public String createAuthorizationCode(String clientIdStr, String scopeStr, User user) {
        //1. 拼装待加密字符串（clientId + scope + 当前精确到毫秒的时间戳）
        String str = clientIdStr + scopeStr + String.valueOf(DateUtil.currentTimeMillis());

        //2. SHA1加密
        String encryptedStr = EncryptUtil.sha1Hex(str);

        //3.1 保存本次请求的授权范围
        iRedisService.setWithExpire(encryptedStr + ":scope", scopeStr, (ExpireEnum.AUTHORIZATION_CODE.getTime()), ExpireEnum.AUTHORIZATION_CODE.getTimeUnit());
        //3.2 保存本次请求所属的用户信息
        iRedisService.setWithExpire(encryptedStr + ":user", user, (ExpireEnum.AUTHORIZATION_CODE.getTime()), ExpireEnum.AUTHORIZATION_CODE.getTimeUnit());

        //4. 返回Authorization Code
        return encryptedStr;
    }

    @Override
    public String createAccessToken(User user, OAuthClientDetail oAuthClientDetail, String grantType, String scope, Long expireIn) {
        //过期时间戳
        Long expireTime = DateUtil.nextDaysSecond(ExpireEnum.ACCESS_TOKEN.getTime(), null);

        //1. 拼装待加密字符串（username + clientId + 当前精确到毫秒的时间戳）
        String str = user.getNickname() + oAuthClientDetail.getClientId() + String.valueOf(DateUtil.currentTimeMillis());

        //2. SHA1加密
        String accessTokenStr = "1." + EncryptUtil.sha1Hex(str) + "." + expireIn + "." + expireTime;

        //3. 保存Access Token
        OAuthAccessToken accessToken = oAuthAccessTokenMapper.selectByUserIdClientIdScope(user.getId(), oAuthClientDetail.getClientId(), scope);
        Date current = new Date();
        //如果存在userId + clientId + scope匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (accessToken == null) {
            accessToken = new OAuthAccessToken();
            accessToken.setId(IDUtil.UUIDStr());
            accessToken.setAccessToken(accessTokenStr);
            accessToken.setUserId(user.getId());
            accessToken.setUserName(user.getNickname());
            accessToken.setClientId(oAuthClientDetail.getId());
            accessToken.setScope(scope);
            accessToken.setExpiresIn(expireTime);
            accessToken.setGrantType(grantType);
            accessToken.setCreateUser(user.getId());
            accessToken.setUpdateUser(user.getId());
            accessToken.setCreateTime(current);
            accessToken.setUpdateTime(current);
            oAuthAccessTokenMapper.insert(accessToken);
        } else {
            accessToken.setAccessToken(accessTokenStr);
            accessToken.setExpiresIn(expireTime);
            accessToken.setUpdateUser(user.getId());
            accessToken.setUpdateTime(current);
            oAuthAccessTokenMapper.updateByPrimaryKeySelective(accessToken);
        }
        return accessTokenStr;
    }

    @Override
    public String createRefreshToken(User user, OAuthAccessToken oAuthAccessToken) {
        //过期时间
        Long expireIn = DateUtil.dayToSecond(ExpireEnum.REFRESH_TOKEN.getTime());
        //过期时间戳
        Long expireTime = DateUtil.nextDaysSecond(ExpireEnum.REFRESH_TOKEN.getTime(), null);

        //1. 拼装待加密字符串（username + accessToken + 当前精确到毫秒的时间戳）
        String str = user.getNickname() + oAuthAccessToken.getAccessToken() + String.valueOf(DateUtil.currentTimeMillis());

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
            refreshToken.setCreateUser(user.getId());
            refreshToken.setUpdateUser(user.getId());
            refreshToken.setCreateTime(current);
            refreshToken.setUpdateTime(current);
            oAuthRefreshTokenMapper.insert(refreshToken);
        } else {
            refreshToken.setRefreshToken(refreshTokenStr);
            refreshToken.setExpiresIn(expireTime);
            refreshToken.setUpdateUser(user.getId());
            refreshToken.setUpdateTime(current);
            oAuthRefreshTokenMapper.updateByPrimaryKeySelective(refreshToken);
        }
        return refreshTokenStr;
    }
}
