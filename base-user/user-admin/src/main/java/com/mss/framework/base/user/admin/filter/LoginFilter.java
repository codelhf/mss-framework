package com.mss.framework.base.user.admin.filter;


import com.mss.framework.base.user.admin.common.RequestHolder;
import com.mss.framework.base.user.admin.pojo.SysUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        SysUser sysUser = (SysUser) request.getSession().getAttribute("user");
        if (sysUser == null){
            String path = "/signin.jsp";
            response.sendRedirect(path);
            return;
        }
        RequestHolder.add(sysUser);
        RequestHolder.add(request);
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    @Override
    public void destroy() {

    }
}
