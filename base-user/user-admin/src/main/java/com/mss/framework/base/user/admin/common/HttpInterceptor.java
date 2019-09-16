package com.mss.framework.base.user.admin.common;

import com.mss.framework.base.user.admin.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class HttpInterceptor implements HandlerInterceptor {

    private static String START_TIME = "requestStartTime";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String url = httpServletRequest.getRequestURI();
        Map parameterMap = httpServletRequest.getParameterMap();
        log.info("request start. url:{}, params:{}", url, JsonMapper.obj2Str(parameterMap));
        long startTime = System.currentTimeMillis();
        httpServletRequest.setAttribute(START_TIME, startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//        String url = httpServletRequest.getRequestURI();
//        long startTime = (long) httpServletRequest.getAttribute(START_TIME);
//        long endTime = System.currentTimeMillis();
//        log.info("request final. url:{}, cost:{}", url, endTime - startTime);
        removeThreadLocalInfo();
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        String url = httpServletRequest.getRequestURI();
        long startTime = (long) httpServletRequest.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        log.info("request completion. url:{}, cost:{}", url, endTime - startTime);
        removeThreadLocalInfo();
    }

    private void removeThreadLocalInfo(){
        RequestHolder.remove();
    }
}
