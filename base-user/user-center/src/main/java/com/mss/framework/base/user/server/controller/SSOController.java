package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.SpringContextUtil;
import com.mss.framework.base.core.util.DateUtil;
import com.mss.framework.base.user.server.common.Constants;
import com.mss.framework.base.user.server.enums.ErrorCodeEnum;
import com.mss.framework.base.user.server.enums.ExpireEnum;
import com.mss.framework.base.user.server.pojo.SSOAccessToken;
import com.mss.framework.base.user.server.pojo.SSOClientDetail;
import com.mss.framework.base.user.server.pojo.SSORefreshToken;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.redis.RedisService;
import com.mss.framework.base.user.server.service.SSOService;
import com.mss.framework.base.user.server.service.UserService;
import com.mss.framework.base.user.server.util.ResponseUtil;
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
import java.util.HashMap;
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
    private RedisService redisService;

    @Autowired
    private SSOService ssoService;
    @Autowired
    private UserService userService;

    @GetMapping("/token")
    public ModelAndView token(String redirectUri, HttpServletRequest request) {
        //过期时间
        Long expiresIn = DateUtil.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());
        //查询接入客户端
        SSOClientDetail ssoAppDetail = ssoService.selectByRedirectUri(redirectUri);
        //获取用户IP
        String requestIP = SpringContextUtil.getRequestIp(request);
        User user = redisService.get(Constants.SESSION_USER);
        //生成Access Token
        String accessTokenStr = ssoService.createAccessToken(user, expiresIn, requestIP, ssoAppDetail);
        //查询已经插入到数据库的Access Token
        SSOAccessToken accessToken = ssoService.selectByAccessToken(accessTokenStr);
        //生成Refresh Token
        String refreshTokenStr = ssoService.createRefreshToken(user, accessToken);

        log.info(MessageFormat.format("单点登录获取Token：username:【{0}】,channel:【{1}】,Access Token:【{2}】,Refresh Token:【{3}】", user.getNickname(), ssoAppDetail.getClientName(), accessTokenStr, refreshTokenStr));
        String params = "?code=" + accessTokenStr;
        return new ModelAndView("redirect:" + redirectUri + params);
    }

    //校验Access Token，并返回用户信息
    @GetMapping("/verify")
    public Map<String, Object> verify(@RequestParam("access_token") String accessTokenStr) {
        //过期时间
        Long expiresIn = DateUtil.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());
        //查询Access Token
        SSOAccessToken accessToken = ssoService.selectByAccessToken(accessTokenStr);
        //查询Refresh Token
        SSORefreshToken refreshToken = ssoService.selectByTokenId(accessToken.getId());
        User user = userService.selectByUserId(accessToken.getUserId());
        //组装返回信息
        Map<String, Object> result = new HashMap<>(8);
        result.put("access_token", accessToken.getAccessToken());
        result.put("refresh_token", refreshToken.getRefreshToken());
        result.put("expires_in", expiresIn);
        result.put("user_info", user);
        return result;
    }

    @GetMapping("/refreshToken")
    public Map<String, Object> refreshToken(@RequestParam("refresh_token") String refreshTokenStr,
                                            HttpServletRequest request) {
        //获取用户IP
        String requestIp = SpringContextUtil.getRequestIp(request);
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
        SSOAccessToken ssoAccessToken = ssoService.selectByAccessId(refreshToken.getTokenId());
        //查询接入客户端
        SSOClientDetail ssoClientDetails = ssoService.selectById(ssoAccessToken.getClientId());
        //获取对应的用户信息
        User user = userService.selectByUserId(ssoAccessToken.getUserId());

        //新的过期时间
        Long expiresIn = DateUtil.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());
        //生成新的Access Token
        String newAccessTokenStr = ssoService.createAccessToken(user, expiresIn, requestIp, ssoClientDetails);

        log.info(MessageFormat.format("单点登录重新刷新Token：username:【{0}】,requestIp:【{1}】,old token:【{2}】,new token:【{3}】", user.getNickname(), requestIp, ssoAccessToken.getAccessToken(), newAccessTokenStr));

        //组装返回信息
        Map<String, Object> result = new HashMap<>(8);
        result.put("access_token", newAccessTokenStr);
        result.put("refresh_token", refreshToken.getRefreshToken());
        result.put("expires_in", expiresIn);
        result.put("user_info", user);
        return result;
    }
}
