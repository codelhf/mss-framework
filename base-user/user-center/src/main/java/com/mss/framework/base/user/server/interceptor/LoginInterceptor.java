package com.mss.framework.base.user.server.interceptor;

import com.mss.framework.base.user.server.common.Constants;
import com.mss.framework.base.user.server.common.SpringContextUtil;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 定义一些页面需要做登录检查
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 11:11
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private IRedisService iRedisService;

    /**
     * 检查是否已经登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取session中存储的token
        User user = iRedisService.get(Constants.SESSION_USER);

        if(user != null){
            return true;
        }
        //如果token不存在，则跳转到登录页面
        response.sendRedirect(request.getContextPath() + "/login?redirectUri=" + SpringContextUtil.getRequestUrl(request));

        return false;
    }
}
