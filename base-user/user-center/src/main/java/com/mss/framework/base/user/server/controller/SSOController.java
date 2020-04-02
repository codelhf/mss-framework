package com.mss.framework.base.user.server.controller;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.core.token.TokenUser;
import com.mss.framework.base.core.token.UserUtil;
import com.mss.framework.base.core.util.DateUtil;
import com.mss.framework.base.core.util.IPUtil;
import com.mss.framework.base.user.server.enums.ErrorCodeEnum;
import com.mss.framework.base.user.server.enums.ExpireEnum;
import com.mss.framework.base.user.server.pojo.SSOAccessToken;
import com.mss.framework.base.user.server.pojo.SSOClientDetail;
import com.mss.framework.base.user.server.pojo.SSORefreshToken;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.SSOService;
import com.mss.framework.base.user.server.service.UserService;
import com.mss.framework.base.user.server.util.ResponseUtil;
import com.mss.framework.base.user.server.web.token.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Description: sso认证相关API
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 9:51
 */
@RestController
@RequestMapping("/sso")
@Slf4j
public class SSOController {

    @Autowired
    private SSOService ssoService;
    @Autowired
    private UserService userService;

    @GetMapping("/token")
    public ModelAndView token(String redirectUri, HttpServletRequest request) {
        //查询接入客户端
        SSOClientDetail ssoClientDetail = ssoService.selectByRedirectUri(redirectUri);
        //获取用户IP
        String requestIP = IPUtil.getRealIp(request);
        //过期时间
        Long expiresIn = DateUtil.dayToMillis(ExpireEnum.ACCESS_TOKEN.getTime());
        //获取用户信息(此时已登录)
        TokenUser tokenUser = UserUtil.getCurrentUser();
        User user = userService.selectByUserId(tokenUser.getId());
        //生成Access Token
        String accessTokenStr = ssoService.createAccessToken(user, expiresIn, requestIP, ssoClientDetail);
        //查询已经插入到数据库的Access Token
        SSOAccessToken accessToken = ssoService.selectByAccessToken(accessTokenStr);
        //生成Refresh Token
        String refreshTokenStr = ssoService.createRefreshToken(user, accessToken.getId());

        log.info(MessageFormat.format("单点登录获取Token：username:【{0}】,channel:【{1}】,Access Token:【{2}】,Refresh Token:【{3}】", tokenUser.getUsername(), ssoClientDetail.getClientName(), accessTokenStr, refreshTokenStr));
        String params = "?code=" + accessTokenStr;
        return new ModelAndView("redirect:" + redirectUri + params);
    }

    //校验Access Token，并返回用户信息
    @GetMapping("/verify")
    public Map<String, Object> verify(@RequestParam("access_token") String accessTokenStr) {
        //过期时间
        Long expiresIn = DateUtil.dayToMillis(ExpireEnum.ACCESS_TOKEN.getTime());
        //查询Access Token
        SSOAccessToken accessToken = ssoService.selectByAccessToken(accessTokenStr);
        //查询Refresh Token
        SSORefreshToken refreshToken = ssoService.selectByTokenId(accessToken.getId());
        //查询用户信息
        User user = userService.selectByUserId(accessToken.getUserId());
        return ResponseUtil.SSOData(accessToken.getAccessToken(), refreshToken.getRefreshToken(), expiresIn, JSON.toJSONString(user));
    }

    @GetMapping("/refreshToken")
    public Map<String, Object> refreshToken(@RequestParam("refresh_token") String refreshTokenStr, HttpServletRequest request) {

        SSORefreshToken refreshToken = ssoService.selectByRefreshToken(refreshTokenStr);
        if (refreshToken == null) {
            return ResponseUtil.errorResponse(ErrorCodeEnum.INVALID_GRANT);
        }
        Long savedExpiresAt = refreshToken.getExpiresIn();
        //过期日期
        LocalDateTime expiresDateTime = DateUtil.ofEpochSecond(savedExpiresAt, null);
        //当前日期
        LocalDateTime nowDateTime = DateUtil.currentTime();

        //如果Refresh Token已经失效，则需要重新生成
        if (expiresDateTime.isBefore(nowDateTime)) {
            return ResponseUtil.errorResponse(ErrorCodeEnum.EXPIRED_TOKEN);
        }

        //获取存储的Access Token
        SSOAccessToken accessToken = ssoService.selectByAccessId(refreshToken.getTokenId());
        //查询接入客户端
        SSOClientDetail clientDetail = ssoService.selectById(accessToken.getClientId());
        //获取对应的用户信息
        User user = userService.selectByUserId(accessToken.getUserId());
        //获取用户IP
        String requestIp = IPUtil.getRealIp(request);
        //新的过期时间
        Long expiresIn = DateUtil.dayToMillis(ExpireEnum.ACCESS_TOKEN.getTime());
        //生成新的Access Token
        String newAccessTokenStr = ssoService.createAccessToken(user, expiresIn, requestIp, clientDetail);

        log.info(MessageFormat.format("单点登录重新刷新Token：username:【{0}】,requestIp:【{1}】,old token:【{2}】,new token:【{3}】", user.getNickname(), requestIp, accessToken.getAccessToken(), newAccessTokenStr));
        return ResponseUtil.SSOData(newAccessTokenStr, refreshTokenStr, expiresIn, JSON.toJSONString(user));
    }
}
