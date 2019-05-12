package com.mss.framework.base.user.oauth.interceptor;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.user.oauth.common.Constants;
import com.mss.framework.base.user.oauth.enums.ErrorCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 用于校验OAuth2.0登录中的状态码
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 22:58
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        //当前系统请求认证服务器成功之后返回的Authorization Code
        String code = request.getParameter("code");
        //原样返回的状态码
        String resultStatus = request.getParameter("status");

        //code不为空，则说明当前请求是从认证服务器返回的回调请求
        if (StringUtils.isNoneBlank(code)) {
            //从session获取保存的状态码
            String savedStatus = (String) session.getAttribute(Constants.SESSION_AUTH_CODE_STATUS);
            //1. 校验状态码是否匹配
            if (savedStatus != null && resultStatus != null && savedStatus.equals(resultStatus)) {
                return true;
            } else {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                Map<String, String> result = new HashMap<>(2);
                result.put("error", ErrorCodeEnum.INVALID_STATUS.getError());
                result.put("error_description", ErrorCodeEnum.INVALID_STATUS.getErrorDescription());

                response.getWriter().write(JSON.toJSONString(result));
                return false;
            }
        }
        return true;
    }
}
