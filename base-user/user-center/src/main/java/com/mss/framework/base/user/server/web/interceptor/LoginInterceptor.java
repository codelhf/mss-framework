package com.mss.framework.base.user.server.web.interceptor;

import com.mss.framework.base.core.common.SpringContextUtil;
import com.mss.framework.base.user.server.common.Constants;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.RedisService;
import com.mss.framework.base.user.server.web.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 定义一些页面需要做登录检查
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 11:11
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService iRedisService;

    /**
     * 检查是否已经登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取session中存储的token
        User user = iRedisService.get(Constants.SESSION_USER);

        if(user != null){
            RequestHolder.add(user);
            RequestHolder.add(request);
            return true;
        }
        //如果token不存在，则跳转到登录页面
        response.sendRedirect(request.getContextPath() + "/login?redirectUri=" + SpringContextUtil.getRequestUrl(request));

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        RequestHolder.remove();
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        RequestHolder.remove();
    }
}
