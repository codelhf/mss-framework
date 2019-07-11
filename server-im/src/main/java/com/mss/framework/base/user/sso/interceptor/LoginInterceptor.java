package com.mss.framework.base.user.sso.interceptor;

import com.mss.framework.base.user.sso.common.Constants;
import com.mss.framework.base.user.sso.common.SpringContextUtils;
import com.mss.framework.base.user.sso.model.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description: 定义一些页面需要做登录检查
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 23:17
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    /**
     * 检查是否已经登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        //获取session中存储的用户信息
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        if(user != null){
            return true;
        }else{
            //如果token不存在，则跳转等登录页面
            response.sendRedirect(request.getContextPath() + "/login?redirectUrl=" + SpringContextUtils.getRequestUrl(request));

            return false;
        }
    }
}
