package com.mss.framework.base.user.oauth.interceptor;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.user.oauth.common.Constants;
import com.mss.framework.base.user.oauth.util.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Description: 定义一些页面需要做登录检查
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 22:59
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 检查是否已经登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        //获取session中存储的token
        String accessToken = (String) session.getAttribute(Constants.SESSION_ACCESS_TOKEN);
        if (StringUtils.isBlank(accessToken)) {
            //如果token不存在,用户未登录
            noLogin(response, "用户未登录");
            return false;
        }
        return true;
    }

    private void noLogin(HttpServletResponse response, String message) throws IOException {
        response.reset();//这里要添加reset，否则报异常 getWriter() has already been called for this response.
        response.setCharacterEncoding("utf-8");//这里要设置编码，否则会乱码
        response.setHeader("Content-Type", "application/json;UTF-8");//这里要设置返回值的类型，因为全部是json接口。
        response.getWriter().print(JSON.toJSONString(JsonResult.error(message)));
    }
}
