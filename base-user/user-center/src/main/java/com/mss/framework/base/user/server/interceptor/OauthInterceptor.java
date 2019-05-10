package com.mss.framework.base.user.server.interceptor;

import com.mss.framework.base.user.server.common.Constants;
import com.mss.framework.base.user.server.dao.OAuthClientDetailMapper;
import com.mss.framework.base.user.server.dao.OAuthClientUserMapper;
import com.mss.framework.base.user.server.dao.OAuthScopeMapper;
import com.mss.framework.base.user.server.enums.ErrorCodeEnum;
import com.mss.framework.base.user.server.pojo.OAuthClientDetail;
import com.mss.framework.base.user.server.pojo.OAuthClientUser;
import com.mss.framework.base.user.server.pojo.OAuthScope;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.util.JsonUtil;
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
public class OauthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private OAuthClientDetailMapper oAuthClientDetailMapper;
    @Autowired
    private OAuthClientUserMapper oAuthClientUserMapper;
    @Autowired
    private OAuthScopeMapper oAuthScopeMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        //参数信息
        String params = "?redirectUri=" + SpringContextUtil.getRequestUrl(request);

        //客户端ID
        String clientIdStr = request.getParameter("client_id");
        //权限范围
        String scopeStr = request.getParameter("scope");
        //回调URL
        String redirectUri = request.getParameter("redirect_uri");
        //返回形式
        String responseType = request.getParameter("response_type");

        //获取session中存储的token
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        if(StringUtils.isNoneBlank(clientIdStr) && StringUtils.isNoneBlank(scopeStr) && StringUtils.isNoneBlank(redirectUri) && "code".equals(responseType)){
            params = params + "&client_id=" + clientIdStr + "&scope=" + scopeStr;

            //1. 查询是否存在授权信息
            OAuthClientDetail clientDetails = oAuthClientDetailMapper.selectByClientId(clientIdStr);
            OAuthScope scope = oAuthScopeMapper.selectByScopeName(scopeStr);

            if(clientDetails == null){
                return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_CLIENT);
            }

            if(scope == null){
                return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_SCOPE);
            }

            if(!clientDetails.getRedirectUri().equals(redirectUri)){
                return this.generateErrorResponse(response, ErrorCodeEnum.REDIRECT_URI_MISMATCH);
            }

            //2. 查询用户给接入的客户端是否已经授权
            OAuthClientUser clientUser = oAuthClientUserMapper.selectByExample(clientDetails.getId(), user.getId(), scope.getId());
            if(clientUser != null){
                return true;
            }
            //如果没有授权，则跳转到授权页面
            response.sendRedirect(request.getContextPath() + "/oauth2.0/authorizePage" + params);
            return false;
        }
        return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_REQUEST);
    }

    /**
     * 组装错误请求的返回
     */
    private boolean generateErrorResponse(HttpServletResponse response, ErrorCodeEnum errorCodeEnum) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        Map<String,String> result = new HashMap<>(2);
        result.put("error", errorCodeEnum.getCode());
        result.put("error_description",errorCodeEnum.getDesc());

        response.getWriter().write(JsonUtil.toJson(result));
        return false;
    }
}
