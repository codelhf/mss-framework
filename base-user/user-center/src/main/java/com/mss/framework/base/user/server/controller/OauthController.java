package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.token.TokenUser;
import com.mss.framework.base.core.token.TokenUtil;
import com.mss.framework.base.core.token.UserUtil;
import com.mss.framework.base.core.util.DateUtil;
import com.mss.framework.base.user.server.common.Constants;
import com.mss.framework.base.user.server.enums.ErrorCodeEnum;
import com.mss.framework.base.user.server.enums.ExpireEnum;
import com.mss.framework.base.user.server.enums.GrantTypeEnum;
import com.mss.framework.base.user.server.pojo.OAuthAccessToken;
import com.mss.framework.base.user.server.pojo.OAuthAppDetail;
import com.mss.framework.base.user.server.pojo.OAuthRefreshToken;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.OAuthService;
import com.mss.framework.base.user.server.service.UserService;
import com.mss.framework.base.user.server.util.JsonUtil2;
import com.mss.framework.base.user.server.util.OAuthUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 基于oauth2.0相关的授权相关操作
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 22:51
 */
@RestController
@RequestMapping("/oauth2.0")
public class OauthController {

    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private UserService userService;

    /**
     * @param [appDetail]
     * @return java.util.Map
     * @description: 注册需要接入的客户端信息
     * @author liuhf
     * @createtime 2019/5/3 23:20
     */
    @PostMapping("/appRegister")
    public Map<String, Object> clientRegister(@RequestBody OAuthAppDetail appDetail) {
        OAuthAppDetail oAuthAppDetail = oAuthService.register(appDetail);
        if (oAuthAppDetail == null) {
            return OAuthUtil.errorMessage("注册失败");
        }
        return OAuthUtil.success(oAuthAppDetail);
    }

    /**
     * @param [appId, scope, redirectUri]
     * @return org.springframework.web.servlet.ModelAndView
     * @description: 授权页面
     * @author liuhf
     * @createtime 2019/5/3 23:21
     */
    @GetMapping("/authorizePage")
    public void authorizePage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //app_id
        String appId = request.getParameter("app_id");
        //scope
        String scope = request.getParameter("scope");
        //redirectUri
        String redirectUri = request.getParameter("redirect_uri");

        if (StringUtils.isNotBlank(redirectUri)) {
            //将申请授权的服务的回调地址添加到session中
            request.getSession().setAttribute(Constants.SESSION_AUTH_REDIRECT_URL, redirectUri);
        }
        //查询请求授权的客户端信息
        OAuthAppDetail oAuthAppDetail = oAuthService.selectByAppId(appId);
        //将申请授权的服务的客户端跳转到授权页
        String params = "appId=" + appId + "&appName=" + oAuthAppDetail.getAppName() + "&scope=" + scope;
        //授权页会有用户未登录的情况,登录后再次跳转回授权页
        response.sendRedirect("/page/authorize.html?" + params);
//        return modelAndView;
    }

    /**
     * @param [session, appId, scope]
     * @return java.util.Map
     * @description: 授权页同意授权
     * @author liuhf
     * @createtime 2019/5/3 23:21
     */
    @PostMapping("/agree")
    public Map<String, Object> agree(HttpSession session, String appId, String scopeId) {
        if (StringUtils.isAnyBlank(appId, scopeId)) {
            return OAuthUtil.errorMessage("appId或scopeId不能为空");
        }
        //1. 向数据库中保存授权信息
        boolean success = oAuthService.saveOAuthAppUser(UserUtil.getCurrentUser().getId(), appId, scopeId);
        //2. 返回给页面的数据
        if (!success) {
            return OAuthUtil.errorMessage("授权失败");
        }
        //授权成功之后的回调地址
        String redirectUri = (String) session.getAttribute(Constants.SESSION_AUTH_REDIRECT_URL);
        session.removeAttribute(Constants.SESSION_AUTH_REDIRECT_URL);
        return OAuthUtil.success(redirectUri);
    }

    /**
     * @param [appId, scope, redirectUri, status]
     * @return org.springframework.web.servlet.ModelAndView
     * @description: 获取Authorization Code
     * @author liuhf
     * @createtime 2019/5/3 23:21
     */
    @GetMapping("/authorize")
    public void authorizeCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //app_id
        String appId = request.getParameter("app_id");
        //scope
        String scope = request.getParameter("scope");
        //redirectUri
        String redirectUri = request.getParameter("redirect_uri");
        //state，用于防止CSRF攻击（非必填）
        String state = request.getParameter("state");
        //生成Authorization Code
        String authorizationCode = oAuthService.createAuthorizationCode(appId, scope, UserUtil.getCurrentUser());

        String params = "?code=" + authorizationCode;
        if (StringUtils.isNoneBlank(state)) {
            params = params + "&state=" + state;
        }
        //重定向到第三方的服务地址
        response.sendRedirect(redirectUri + params);
//        return new ModelAndView("redirect:" + redirectUri + params);
    }

    /**
     * @param [request]
     * @return java.util.Map
     * @description: 通过Authorization Code获取Access Token
     * @author liuhf
     * @createtime 2019/5/3 23:27
     */
    @GetMapping("/token")
    public Map<String, Object> token(HttpServletRequest request) {
        //授权方式
        String grantType = request.getParameter("grant_type");
        //前面获取的Authorization Code
        String code = request.getParameter("code");
        //appID
        String appId = request.getParameter("app_id");
        //接入的app的密钥
        String appSecret = request.getParameter("app_secret");
        //回调URL
        String redirectUri = request.getParameter("redirect_uri");

        //校验授权方式
        if (!GrantTypeEnum.AUTHORIZATION_CODE.getType().equals(grantType)) {
            return OAuthUtil.errorResponse(ErrorCodeEnum.UNSUPPORTED_GRANT_TYPE);
        }
        OAuthAppDetail oAuthAppDetail = oAuthService.selectByAppId(appId);
        //校验请求的客户端秘钥和已保存的秘钥是否匹配
        if (!(oAuthAppDetail != null && oAuthAppDetail.getAppSecret().equals(appSecret))) {
            return OAuthUtil.errorResponse(ErrorCodeEnum.INVALID_CLIENT);
        }

        //校验回调URL
        if (!oAuthAppDetail.getRedirectUri().equals(redirectUri)) {
            return OAuthUtil.errorResponse(ErrorCodeEnum.REDIRECT_URI_MISMATCH);
        }
        TokenUser tokenUser = TokenUtil.verify(code);
        //如果能够通过Authorization Code获取到对应的用户信息，则说明该Authorization Code有效
        if (tokenUser == null || tokenUser.getScope() == null) {
            return OAuthUtil.errorResponse(ErrorCodeEnum.INVALID_GRANT);
        }
        //过期时间
        Long expiresIn = DateUtil.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());

        //生成Access Token
        String accessToken = oAuthService.createAccessToken(tokenUser, oAuthAppDetail, grantType, tokenUser.getScope(), expiresIn);
        //查询已经插入到数据库的Access Token
        OAuthAccessToken authAccessToken = oAuthService.selectByAccessToken(accessToken);
        //生成Refresh Token
        String refreshToken = oAuthService.createRefreshToken(tokenUser, authAccessToken);
        Map<String, Object> result = new HashMap<>(8);
        //返回数据
        result.put("access_token", authAccessToken.getAccessToken());
        result.put("refresh_token", refreshToken);
        result.put("expires_in", expiresIn);
        result.put("scope", tokenUser.getScope());
        return result;
    }

    /**
     * @param [accessToken]
     * @return java.lang.String
     * @description: 校验accessToken获取用户信息
     * @author liuhf
     * @createtime 2019/5/4 9:31
     */
    @GetMapping(value = "/verify", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getInfo(@RequestParam("access_token") String accessToken) {
        OAuthAccessToken oAuthAccessToken = oAuthService.selectByAccessToken(accessToken);
        if (oAuthAccessToken == null) {
            return JsonUtil2.toJson(OAuthUtil.errorResponse(ErrorCodeEnum.INVALID_GRANT));
        }
        User user = userService.selectUserInfoByScope(oAuthAccessToken.getUserId(), oAuthAccessToken.getScope());
        return JsonUtil2.toJson(user);
    }

    /**
     * @param [request]
     * @return java.util.Map
     * @description: 通过Refresh Token刷新Access Token
     * @author liuhf
     * @createtime 2019/5/3 23:27
     */
    @GetMapping("/refreshToken")
    public Map<String, Object> refreshToken(HttpServletRequest request) {
        //获取Refresh Token
        String refreshTokenStr = request.getParameter("refresh_token");
        OAuthRefreshToken authRefreshToken = oAuthService.selectByRefreshToken(refreshTokenStr);

        if (authRefreshToken == null) {
            return OAuthUtil.errorResponse(ErrorCodeEnum.INVALID_GRANT);
        }
        Long savedExpiresAt = authRefreshToken.getExpiresIn();
        //过期日期
        LocalDateTime expiresDateTime = DateUtil.ofEpochSecond(savedExpiresAt, null);
        //当前日期
        LocalDateTime nowDateTime = DateUtil.currentTime();

        //如果Refresh Token已经失效，则需要重新生成
        if (expiresDateTime.isBefore(nowDateTime)) {
            return OAuthUtil.errorResponse(ErrorCodeEnum.EXPIRED_TOKEN);
        }
        //获取存储的Access Token
        OAuthAccessToken authAccessToken = oAuthService.selectByAccessId(authRefreshToken.getTokenId());
        //获取对应的客户端信息
        OAuthAppDetail savedClientDetail = oAuthService.selectById(authAccessToken.getAppId());
        //获取对应的用户信息
        User user = userService.selectByUserId(authAccessToken.getUserId());
        TokenUser tokenUser = new TokenUser();
        tokenUser.setId(user.getId());
        tokenUser.setUsername(user.getNickname());
        //新的过期时间
        Long expiresIn = DateUtil.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());
        //生成新的Access Token
        String newAccessTokenStr = oAuthService.createAccessToken(tokenUser, savedClientDetail, authAccessToken.getGrantType(), authAccessToken.getScope(), expiresIn);
        Map<String, Object> result = new HashMap<>(8);
        //返回数据
        result.put("access_token", newAccessTokenStr);
        result.put("refresh_token", refreshTokenStr);
        result.put("expires_in", expiresIn);
        result.put("scope", authAccessToken.getScope());
        return result;
    }
}
