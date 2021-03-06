package com.mss.framework.base.user.server.web.interceptor;

import com.mss.framework.base.core.common.ResponseCode;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.core.token.TokenUser;
import com.mss.framework.base.core.token.UserUtil;
import com.mss.framework.base.user.server.redis.RedisUtil;
import com.mss.framework.base.user.server.web.token.TokenUtil;
import com.mss.framework.base.user.server.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 定义一些页面需要做登录检查
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 11:11
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 检查是否已经登录
     */
    @Override//在请求处理之前进行调用(Controller方法调用之前)
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String accessToken = TokenUtil.readAccessToken(request);
        if (StringUtils.isBlank(accessToken)) {
            //如果token不存在，则跳转到登录页面
            noLogin(response);
            return false;
        }
        TokenUser tokenUser = TokenUtil.getTokenUser(accessToken);
        if(tokenUser == null) {
            //token已过期
            noLogin(response);
            return false;
        }
//        String jsonUser = RedisUtil.getKey(tokenUser.getId());
//        if (StringUtils.isNotBlank(jsonUser)) {
//            //用户已登出
//            noLogin(response);
//            return false;
//        }
        UserUtil.add(tokenUser);
        UserUtil.add(request);
        return true;
    }

    @Override//请求处理之后进行调用，但是在视图被渲染之前(Controller)方法调用之后
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        UserUtil.remove();
    }

    @Override//在整个请求结束之后被调用，也就是在DispatcherServlet
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        UserUtil.remove();
    }

    private void noLogin(HttpServletResponse response) throws IOException {
        String message = JsonUtil.objToStr(ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()));
        permissionMessage(response, message);
    }

    private void noPermission(HttpServletResponse response, String message) throws IOException {
        message = JsonUtil.objToStr(ServerResponse.createByErrorMessage(message));
        permissionMessage(response, message);
    }

    private void permissionMessage(HttpServletResponse response, String message) throws IOException {
        response.reset();//这里要添加reset，否则报异常 getWriter() has already been called for this response.
        response.setCharacterEncoding("utf-8");//这里要设置编码，否则会乱码
        response.setHeader("Content-Type", "application/json;UTF-8");//这里要设置返回值的类型，因为全部是json接口。
        response.getWriter().print(message);
    }
}
