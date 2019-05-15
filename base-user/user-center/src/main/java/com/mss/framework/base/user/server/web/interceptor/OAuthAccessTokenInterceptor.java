package com.mss.framework.base.user.server.web.interceptor;

import com.mss.framework.base.core.util.DateUtil;
import com.mss.framework.base.user.server.enums.ErrorCodeEnum;
import com.mss.framework.base.user.server.pojo.OAuthAccessToken;
import com.mss.framework.base.user.server.service.OAuthService;
import com.mss.framework.base.user.server.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 用于校验Access Token是否为空以及Access Token是否已经失效
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 11:07
 */
public class OAuthAccessTokenInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private OAuthService ioAuthService;

    /**
     * 检查Access Token是否已经失效
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getParameter("access_token");

        if(StringUtils.isNoneBlank(accessToken)){
            //查询数据库中的Access Token
            OAuthAccessToken authAccessToken = ioAuthService.selectByAccessToken(accessToken);

            if(authAccessToken != null){
                Long savedExpiresAt = authAccessToken.getExpiresIn();
                //过期日期
                LocalDateTime expiresDateTime = DateUtil.ofEpochSecond(savedExpiresAt, null);
                //当前日期
                LocalDateTime nowDateTime = DateUtil.now();

                //如果Access Token已经失效，则返回错误提示
                return expiresDateTime.isAfter(nowDateTime) || this.generateErrorResponse(response, ErrorCodeEnum.EXPIRED_TOKEN);
            }else{
                return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_GRANT);
            }
        }else{
            return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_REQUEST);
        }
    }

    /**
     * 组装错误请求的返回
     */
    private boolean generateErrorResponse(HttpServletResponse response, ErrorCodeEnum errorCodeEnum) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        Map<String,String> result = new HashMap<>(2);
        result.put("error", errorCodeEnum.getCode());
        result.put("error_description",errorCodeEnum.getDesc());

        response.getWriter().write(JsonUtil.toJson(result));
        return false;
    }
}
