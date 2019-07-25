package com.mss.framework.base.user.server.web.interceptor;

import com.mss.framework.base.user.server.dao.SSOAppDetailMapper;
import com.mss.framework.base.user.server.enums.ErrorCodeEnum;
import com.mss.framework.base.user.server.pojo.SSOAppDetail;
import com.mss.framework.base.user.server.util.JsonUtil2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 用于校验请求获取Token的回调URL是否在白名单中
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 11:22
 */
//@Component
public class SSOInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SSOAppDetailMapper ssoAppDetailMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String redirectUri = request.getParameter("redirect_uri");

        if (StringUtils.isBlank(redirectUri)) {
            return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_REQUEST);
        }

        //查询数据库中的回调地址的白名单
        SSOAppDetail ssoAppDetail = ssoAppDetailMapper.selectByRedirectUrl(redirectUri);
        if(ssoAppDetail == null){
            //如果回调URL不在白名单中，则返回错误提示
            return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_REDIRECT_URI);
        }
        return true;
    }

    /**
     * 组装错误请求的返回
     */
    private boolean generateErrorResponse(HttpServletResponse response, ErrorCodeEnum errorCodeEnum) throws Exception {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");

        Map<String,String> result = new HashMap<>(2);
        result.put("error", errorCodeEnum.getCode());
        result.put("error_description",errorCodeEnum.getDesc());

        response.getWriter().write(JsonUtil2.toJson(result));
        return false;
    }
}
