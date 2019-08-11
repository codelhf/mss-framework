package com.mss.framework.base.user.server.service;

import com.mss.framework.base.core.token.TokenUser;
import com.mss.framework.base.user.server.pojo.SSOAccessToken;
import com.mss.framework.base.user.server.pojo.SSOClientDetail;
import com.mss.framework.base.user.server.pojo.SSORefreshToken;

/**
 * @Description: SSO单点登录相关Service
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 9:54
 */
public interface SSOService {

    /**
     * @description: 根据ID查询接入客户端
     * @author liuhf
     * @createtime 2019/5/4 10:44
     *
     * @param [id]
     * @return SSOAppDetail
     */
    SSOClientDetail selectById(String id);

    /**
     * @description: 根据URL查询记录
     * @author liuhf
     * @createtime 2019/5/4 10:43
     *
     * @param [redirectUri]
     * @return SSOAppDetail
     */
    SSOClientDetail selectByRedirectUri(String redirectUri);

    /**
     * @description: 通过主键ID查询记录
     * @author liuhf
     * @createtime 2019/5/4 10:43
     *
     * @param [id]
     * @return SSOAccessToken
     */
    SSOAccessToken selectByAccessId(String id);

    /**
     * @description: 通过Access Token查询记录
     * @author liuhf
     * @createtime 2019/5/4 10:43
     *
     * @param [accessToken]
     * @return SSOAccessToken
     */
    SSOAccessToken selectByAccessToken(String accessToken);

    /**
     * @description: 通过tokenId查询记录
     * @author liuhf
     * @createtime 2019/5/4 10:43
     *
     * @param [tokenId]
     * @return SSORefreshToken
     */
    SSORefreshToken selectByTokenId(String tokenId);

    /**
     * @description: 通过Refresh Token查询记录
     * @author liuhf
     * @createtime 2019/5/4 10:43
     *
     * @param [refreshToken]
     * @return SSORefreshToken
     */
    SSORefreshToken selectByRefreshToken(String refreshToken);

    /**
     * @description: 生成Access Token
     * @author liuhf
     * @createtime 2019/5/4 10:43
     *
     * @param [user, expireIn, requestIP, ssoClientDetail]
     * @return java.lang.String
     */
    String createAccessToken(TokenUser tokenUser, Long expireIn, String requestIP, SSOClientDetail ssoClientDetail);

    /**
     * @description: 生成Refresh Token
     * @author liuhf
     * @createtime 2019/5/4 10:42
     *
     * @param [user, ssoAccessToken]
     * @return java.lang.String
     */
    String createRefreshToken(TokenUser tokenUser, SSOAccessToken ssoAccessToken);
}
