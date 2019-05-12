package com.mss.framework.base.user.server.service;

import com.mss.framework.base.user.server.pojo.OAuthAccessToken;
import com.mss.framework.base.user.server.pojo.OAuthClientDetail;
import com.mss.framework.base.user.server.pojo.OAuthRefreshToken;
import com.mss.framework.base.user.server.pojo.User;

/**
 * @Description: 授权相关Service
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 20:25
 */
public interface IOAuthService {

    /**
     * @description: 通过id查询客户端信息
     * @author liuhf
     * @createtime 2019/5/3 22:38
     *
     * @param [id]
     * @return OAuthClientDetail
     */
    OAuthClientDetail selectById(String id);

    /**
     * @description: 通过client_id查询客户端信息
     * @author liuhf
     * @createtime 2019/5/3 22:38
     *
     * @param [clientId]
     * @return OAuthClientDetail
     */
    OAuthClientDetail selectByClientId(String clientId);

    /**
     * @description: 通过Access Token查询记录
     * @author liuhf
     * @createtime 2019/5/3 22:38
     *
     * @param [accessToken]
     * @return OAuthAccessToken
     */
    OAuthAccessToken selectByAccessToken(String accessToken);

    /**
     * @description: 通过主键查询记录
     * @author liuhf
     * @createtime 2019/5/3 22:38
     *
     * @param [id]
     * @return OAuthAccessToken
     */
    OAuthAccessToken selectByAccessId(String id);

    /**
     * @description: 通过Refresh Token查询记录
     * @author liuhf
     * @createtime 2019/5/3 22:39
     *
     * @param [refreshToken]
     * @return OAuthRefreshToken
     */
    OAuthRefreshToken selectByRefreshToken(String refreshToken);

    /**
     * @description: 注册需要接入的客户端信息
     * @author liuhf
     * @createtime 2019/5/3 20:56
     *
     * @param [oAuthClientDetail] 用户传递进来的关键信息
     * @return OAuthClientDetail
     */
    OAuthClientDetail register(OAuthClientDetail oAuthClientDetail);

    /**
     * @description: 保存哪个用户授权哪个接入的客户端哪种访问范围的权限
     * @author liuhf
     * @createtime 2019/5/3 22:39
     *
     * @param [userId, clientIdStr, scopeName]
     * @return boolean
     */
    boolean saveOAuthClientUser(String userId, String clientIdStr, String scopeName);

    /**
     * @description: 根据clientId、scope以及当前时间戳生成AuthorizationCode（有效期为10分钟）
     * @author liuhf
     * @createtime 2019/5/3 22:39
     *
     * @param [clientIdStr, scopeStr, user]
     * @return java.lang.String
     */
    String createAuthorizationCode(String clientIdStr, String scopeStr, User user);

    /**
     * @description: 生成Access Token
     * @author liuhf
     * @createtime 2019/5/3 22:39
     *
     * @param [user, oAuthClientDetail, grantType, scope, expireIn]
     * @return java.lang.String
     */
    String createAccessToken(User user, OAuthClientDetail oAuthClientDetail, String grantType, String scope, Long expiresIn);

    /**
     * @description: 生成Refresh Token
     * @author liuhf
     * @createtime 2019/5/3 22:39
     *
     * @param [user, oAuthAccessToken] 生成的Access Token信息
     * @return java.lang.String
     */
    String createRefreshToken(User user, OAuthAccessToken oAuthAccessToken);
}
