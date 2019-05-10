package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.core.util.DateUtil;
import com.mss.framework.base.user.server.common.Constants;
import com.mss.framework.base.user.server.enums.ErrorCodeEnum;
import com.mss.framework.base.user.server.enums.ExpireEnum;
import com.mss.framework.base.user.server.enums.GrantTypeEnum;
import com.mss.framework.base.user.server.pojo.OAuthAccessToken;
import com.mss.framework.base.user.server.pojo.OAuthClientDetail;
import com.mss.framework.base.user.server.pojo.OAuthRefreshToken;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.IOAuthService;
import com.mss.framework.base.user.server.service.IUserService;
import com.mss.framework.base.user.server.service.IRedisService;
import com.mss.framework.base.user.server.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 基于oauth2.0相关的授权相关操作
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 22:51
 */
@Controller
@RequestMapping("/oauth2.0")
public class OauthController {

    @Autowired
    private IRedisService iRedisService;

    @Autowired
    private IOAuthService ioAuthService;
    @Autowired
    private IUserService iUserService;

    /**
     * @param [clientDetail]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 注册需要接入的客户端信息
     * @author liuhf
     * @createtime 2019/5/3 23:20
     */
    @PostMapping("/clientRegister")
    public ServerResponse<OAuthClientDetail> clientRegister(@RequestBody OAuthClientDetail clientDetail) {
        boolean success = ioAuthService.register(clientDetail);
        if (success) {
            return ServerResponse.createBySuccess(clientDetail);
        }
        return ServerResponse.createByErrorMessage("注册失败");
    }

    /**
     * @param [session, redirectUri, clientId, scope]
     * @return org.springframework.web.servlet.ModelAndView
     * @description: 授权页面
     * @author liuhf
     * @createtime 2019/5/3 23:21
     */
    @GetMapping("/authorizePage")
    public ModelAndView authorizePage(HttpSession session, String redirectUri, String clientId, String scope) {
        if (StringUtils.isNotBlank(redirectUri)) {
            //将回调地址添加到session中
            session.setAttribute(Constants.SESSION_AUTH_REDIRECT_URL, redirectUri);
        }
        ModelAndView modelAndView = new ModelAndView("authorize");
        //查询请求授权的客户端信息
        OAuthClientDetail clientDetail = ioAuthService.selectByClientId(clientId);
        modelAndView.addObject("clientId", clientId);
        modelAndView.addObject("clientName", clientDetail.getClientName());
        modelAndView.addObject("scope", scope);
        return modelAndView;
    }

    /**
     * @param [session, clientId, scope]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 同意授权
     * @author liuhf
     * @createtime 2019/5/3 23:21
     */
    @PostMapping("/agree")
    public Map<String, Object> agree(HttpSession session, String clientId, String scope) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isAnyBlank(clientId, scope)) {
            result.put("msg", "请求参数不能为空！");
        }
        User user = iRedisService.get(Constants.SESSION_USER);
        //1. 向数据库中保存授权信息
        boolean success = ioAuthService.saveOAuthClientUser(user.getId(), clientId, scope);
        //2. 返回给页面的数据
        if (success) {
            result.put("code", 200);
            //授权成功之后的回调地址
            String redirectUri = (String) session.getAttribute(Constants.SESSION_AUTH_REDIRECT_URL);
            session.removeAttribute(Constants.SESSION_AUTH_REDIRECT_URL);
            if (StringUtils.isNotBlank(redirectUri)) ;
                result.put("redirect_uri", redirectUri);
        } else {
            result.put("msg", "授权失败");
        }
        return result;
    }

    /**
     * @param [session, clientId, scope, redirectUri, status]
     * @return org.springframework.web.servlet.ModelAndView
     * @description: 获取Authorization Code
     * @author liuhf
     * @createtime 2019/5/3 23:21
     */
    @GetMapping("/authorizeCode")
    public ModelAndView authorizeCode(String clientId, String scope, String redirectUri,
                                      //status，用于防止CSRF攻击（非必填）
                                      @RequestParam(value = "status", required = false) String status) {
        User user = iRedisService.get(Constants.SESSION_USER);
        //生成Authorization Code
        String authorizationCode = ioAuthService.createAuthorizationCode(clientId, scope, user);
        String params = "?code=" + authorizationCode;
        if (StringUtils.isNoneBlank(status)) {
            params = params + "&status=" + status;
        }
        return new ModelAndView("redirect:" + redirectUri + params);
    }

    /**
     * @param [request]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 通过Authorization Code获取Access Token
     * @author liuhf
     * @createtime 2019/5/3 23:27
     */
    @GetMapping("/token")
    public Map<String, Object> token(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(8);

        //授权方式
        String grantType = request.getParameter("grant_type");
        //前面获取的Authorization Code
        String code = request.getParameter("code");
        //客户端ID
        String clientIdStr = request.getParameter("client_id");
        //接入的客户端的密钥
        String clientSecret = request.getParameter("client_secret");
        //回调URL
        String redirectUri = request.getParameter("redirect_uri");

        //校验授权方式
        if (!GrantTypeEnum.AUTHORIZATION_CODE.getType().equals(grantType)) {
            this.generateErrorResponse(result, ErrorCodeEnum.UNSUPPORTED_GRANT_TYPE);
            return result;
        }

        try {
            OAuthClientDetail savedClientDetails = ioAuthService.selectByClientId(clientIdStr);
            //校验请求的客户端秘钥和已保存的秘钥是否匹配
            if (!(savedClientDetails != null && savedClientDetails.getClientSecret().equals(clientSecret))) {
                this.generateErrorResponse(result, ErrorCodeEnum.INVALID_CLIENT);
                return result;
            }

            //校验回调URL
            if (!savedClientDetails.getRedirectUri().equals(redirectUri)) {
                this.generateErrorResponse(result, ErrorCodeEnum.REDIRECT_URI_MISMATCH);
                return result;
            }

            //从Redis获取允许访问的用户权限范围
            String scope = iRedisService.get(code + ":scope");
            //从Redis获取对应的用户信息
            User user = iRedisService.get(code + ":user");

            //如果能够通过Authorization Code获取到对应的用户信息，则说明该Authorization Code有效
            if (StringUtils.isNoneBlank(scope) && user != null) {
                //过期时间
                Long expiresIn = DateUtil.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());

                //生成Access Token
                String accessTokenStr = ioAuthService.createAccessToken(user, savedClientDetails, grantType, scope, expiresIn);
                //查询已经插入到数据库的Access Token
                OAuthAccessToken authAccessToken = ioAuthService.selectByAccessToken(accessTokenStr);
                //生成Refresh Token
                String refreshTokenStr = ioAuthService.createRefreshToken(user, authAccessToken);

                //返回数据
                result.put("access_token", authAccessToken.getAccessToken());
                result.put("refresh_token", refreshTokenStr);
                result.put("expires_in", expiresIn);
                result.put("scope", scope);
                return result;
            } else {
                this.generateErrorResponse(result, ErrorCodeEnum.INVALID_GRANT);
                return result;
            }
        } catch (Exception e) {
            this.generateErrorResponse(result, ErrorCodeEnum.UNKNOWN_ERROR);
            return result;
        }
    }

    /**
     * @description: 通过accessToken获取用户信息
     * @author liuhf
     * @createtime 2019/5/4 9:31
     *
     * @param [accessToken]
     * @return java.lang.String
     */
    @GetMapping(value = "/getUserInfo/{access_token}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getInfo(@PathVariable("access_token") String accessToken) {
        OAuthAccessToken oAuthAccessToken = ioAuthService.selectByAccessToken(accessToken);
        if (oAuthAccessToken == null) {
            Map<String, String> result = new HashMap<>(2);
            result.put("error", ErrorCodeEnum.INVALID_GRANT.getCode());
            result.put("error_description", ErrorCodeEnum.INVALID_GRANT.getDesc());
            return JsonUtil.toJson(result);
        }
        User user = iUserService.selectUserInfoByScope(oAuthAccessToken.getUserId(), oAuthAccessToken.getScope());
        return JsonUtil.toJson(user);
    }

    /**
     * @param [request]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 通过Refresh Token刷新Access Token
     * @author liuhf
     * @createtime 2019/5/3 23:27
     */
    @GetMapping("/refreshToken")
    public Map<String, Object> refreshToken(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(8);

        //获取Refresh Token
        String refreshTokenStr = request.getParameter("refresh_token");

        try {
            OAuthRefreshToken authRefreshToken = ioAuthService.selectByRefreshToken(refreshTokenStr);

            if (authRefreshToken != null) {
                Long savedExpiresAt = authRefreshToken.getExpiresIn();
                //过期日期
                LocalDateTime expiresDateTime = DateUtil.ofEpochSecond(savedExpiresAt, null);
                //当前日期
                LocalDateTime nowDateTime = DateUtil.now();

                //如果Refresh Token已经失效，则需要重新生成
                if (expiresDateTime.isBefore(nowDateTime)) {
                    this.generateErrorResponse(result, ErrorCodeEnum.EXPIRED_TOKEN);
                    return result;
                } else {
                    //获取存储的Access Token
                    OAuthAccessToken authAccessToken = ioAuthService.selectByAccessId(authRefreshToken.getTokenId());
                    //获取对应的客户端信息
                    OAuthClientDetail savedClientDetails = ioAuthService.selectById(authAccessToken.getClientId());
                    //获取对应的用户信息
                    User user = iUserService.selectByUserId(authAccessToken.getUserId());

                    //新的过期时间
                    Long expiresIn = DateUtil.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());
                    //生成新的Access Token
                    String newAccessTokenStr = ioAuthService.createAccessToken(user, savedClientDetails
                            , authAccessToken.getGrantType(), authAccessToken.getScope(), expiresIn);

                    //返回数据
                    result.put("access_token", newAccessTokenStr);
                    result.put("refresh_token", refreshTokenStr);
                    result.put("expires_in", expiresIn);
                    result.put("scope", authAccessToken.getScope());
                    return result;
                }
            } else {
                this.generateErrorResponse(result, ErrorCodeEnum.INVALID_GRANT);
                return result;
            }
        } catch (Exception e) {
            this.generateErrorResponse(result, ErrorCodeEnum.UNKNOWN_ERROR);
            return result;
        }
    }

    /**
     * 组装错误请求的返回
     */
    private void generateErrorResponse(Map<String, Object> result, ErrorCodeEnum errorCodeEnum) {
        result.put("error", errorCodeEnum.getCode());
        result.put("error_description", errorCodeEnum.getDesc());
    }
}
