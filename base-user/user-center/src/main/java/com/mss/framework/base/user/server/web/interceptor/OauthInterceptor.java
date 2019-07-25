package com.mss.framework.base.user.server.web.interceptor;

import com.mss.framework.base.core.common.SpringContextUtil;
import com.mss.framework.base.user.server.common.Constants;
import com.mss.framework.base.user.server.dao.OAuthAppDetailMapper;
import com.mss.framework.base.user.server.dao.OAuthAppUserMapper;
import com.mss.framework.base.user.server.dao.OAuthScopeMapper;
import com.mss.framework.base.user.server.enums.ErrorCodeEnum;
import com.mss.framework.base.user.server.pojo.OAuthAppDetail;
import com.mss.framework.base.user.server.pojo.OAuthAppUser;
import com.mss.framework.base.user.server.pojo.OAuthScope;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.util.JsonUtil2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 检查是否已经存在授权
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 11:14
 */
//@Component
public class OauthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private OAuthAppDetailMapper oAuthAppDetailMapper;
    @Autowired
    private OAuthAppUserMapper oAuthAppUserMapper;
    @Autowired
    private OAuthScopeMapper oAuthScopeMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        //获取session中存储的token
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        //应用ID
        String appId = request.getParameter("app_id");
        //权限范围
        String scope = request.getParameter("scope");
        //回调URL
        String redirectUri = request.getParameter("redirect_uri");
        //返回形式
        String responseType = request.getParameter("response_type");
        //参数为空并且不是验证码模式
        if (StringUtils.isAnyBlank(appId, scope, redirectUri) || !"code".equals(responseType)) {
            return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_REQUEST);
        }
        //1. 查询是否存在授权信息
        OAuthAppDetail oAuthAppDetail = oAuthAppDetailMapper.selectByAppId(appId);
        OAuthScope oAuthScope = oAuthScopeMapper.selectByScope(scope);

        if (oAuthAppDetail == null) {
            return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_CLIENT);
        }

        if (oAuthScope == null) {
            return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_SCOPE);
        }

        if (!oAuthAppDetail.getRedirectUri().equals(redirectUri)) {
            return this.generateErrorResponse(response, ErrorCodeEnum.REDIRECT_URI_MISMATCH);
        }

        //2. 查询用户给接入的APP是否已经授权
        OAuthAppUser oAuthAppUser = oAuthAppUserMapper.selectByExample(oAuthAppDetail.getId(), user.getId(), oAuthScope.getId());
        if (oAuthAppUser == null) {
            //参数信息
            String params = "?redirectUri=" + SpringContextUtil.getRequestUrl(request);
            params = params + "&app_id=" + appId + "&scope=" + scope;
            //如果没有授权，则跳转到授权页面
            response.sendRedirect(request.getContextPath() + "/oauth2.0/authorizePage" + params);
            return false;
        }
        return true;
    }

    /**
     * 组装错误请求的返回
     */
    private boolean generateErrorResponse(HttpServletResponse response, ErrorCodeEnum errorCodeEnum) throws Exception {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");

        Map<String, String> result = new HashMap<>(2);
        result.put("error", errorCodeEnum.getCode());
        result.put("error_description", errorCodeEnum.getDesc());

        response.getWriter().write(JsonUtil2.toJson(result));
        return false;
    }
}
