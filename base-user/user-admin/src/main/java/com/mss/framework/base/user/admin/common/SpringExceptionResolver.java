package com.mss.framework.base.user.admin.common;

import com.mss.framework.base.user.admin.excepton.ParamException;
import com.mss.framework.base.user.admin.excepton.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        String url = request.getRequestURI();
        ModelAndView mv;
        String defaultMsg = "System Error";
        // .json .page
        if (url.endsWith(".json")) {// 这里我们要求项目中所有请求json数据，都使用.json结尾
            if (e instanceof PermissionException || e instanceof ParamException) {
                JsonData result = JsonData.fail(e.getMessage());
                mv = new ModelAndView("jsonView", result.toMap());
            } else {
                log.error("unknown json exception url:" + url, e);
                JsonData result = JsonData.fail(defaultMsg);
                mv = new ModelAndView("jsonView", result.toMap());
            }
        } else if (url.endsWith(".page")) {// 这里我们要求项目中所有请求page数据，都使用.page结尾
            JsonData result = JsonData.fail(defaultMsg);
            log.error("unknown page exception url:" + url, e);
            mv = new ModelAndView("exception", result.toMap());
        } else {
            JsonData result = JsonData.fail(defaultMsg);
            log.error("unknown exception url:" + url, e);
            mv = new ModelAndView("jsonView", result.toMap());
        }
        return mv;
    }
}
