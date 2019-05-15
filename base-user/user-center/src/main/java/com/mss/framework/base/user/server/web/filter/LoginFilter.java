package com.mss.framework.base.user.server.web.filter;

import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/14 23:41
 */
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String loginToken = "";
        //用户已登录过
        if (StringUtils.isNotEmpty(loginToken)) {
//            String userJsonStr = RedisPoolUtil.get(loginToken);
            String userJsonStr = (String) request.getSession().getAttribute(loginToken);
            User user = JsonUtil.fromJson(userJsonStr, User.class);
            if (user != null){
                //如果user不为空，则重置session的有效期
//                RedisPoolUtil.expire(loginToken, 60*30);
                RequestHolder.add(user);
                RequestHolder.add(request);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
