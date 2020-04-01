package com.mss.framework.base.user.server.web.interceptor;

import com.mss.framework.base.core.token.TokenUser;
import com.mss.framework.base.core.token.UserUtil;
import com.mss.framework.base.core.util.IPUtil;
import com.mss.framework.base.user.server.dao.OAuthClientDetailMapper;
import com.mss.framework.base.user.server.dao.OAuthClientUserMapper;
import com.mss.framework.base.user.server.dao.OAuthScopeMapper;
import com.mss.framework.base.user.server.enums.ErrorCodeEnum;
import com.mss.framework.base.user.server.pojo.OAuthClientDetail;
import com.mss.framework.base.user.server.pojo.OAuthClientUser;
import com.mss.framework.base.user.server.pojo.OAuthScope;
import com.mss.framework.base.user.server.util.JsonUtil2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 检查是否已经存在授权
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 11:14
 */
@Component
public class OAuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private OAuthClientDetailMapper oAuthClientDetailMapper;
    @Autowired
    private OAuthClientUserMapper oAuthClientUserMapper;
    @Autowired
    private OAuthScopeMapper oAuthScopeMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //应用ID
        String clientId = request.getParameter("client_id");
        //权限范围
        String scope = request.getParameter("scope");
        //回调URL
        String redirectUri = request.getParameter("redirect_uri");
        //返回形式
        String responseType = request.getParameter("response_type");
        //参数为空并且不是验证码模式
        if (StringUtils.isAnyBlank(clientId, scope, redirectUri) || !"code".equals(responseType)) {
            return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_REQUEST);
        }
        //1. 查询是否存在授权信息
        OAuthClientDetail oAuthClientDetail = oAuthClientDetailMapper.selectByClientId(clientId);
        if (oAuthClientDetail == null) {
            return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_CLIENT);
        }
        if (!oAuthClientDetail.getRedirectUri().equals(redirectUri)) {
            return this.generateErrorResponse(response, ErrorCodeEnum.REDIRECT_URI_MISMATCH);
        }
        OAuthScope oAuthScope = oAuthScopeMapper.selectByScope(scope);
        if (oAuthScope == null) {
            return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_SCOPE);
        }
        //获取登录用户信息
        TokenUser tokenUser = UserUtil.getCurrentUser();
        if (tokenUser == null) {
            return true;
        }
        //2. 查询用户给接入的APP是否已经授权
        OAuthClientUser oAuthClientUser = oAuthClientUserMapper.selectByExample(oAuthClientDetail.getId(), tokenUser.getId(), oAuthScope.getId());
        if (oAuthClientUser == null) {
            //参数信息
            String params = "?redirectUri=" + IPUtil.getRealIp(request);
            params = params + "&client_id=" + clientId + "&scope=" + scope;
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
