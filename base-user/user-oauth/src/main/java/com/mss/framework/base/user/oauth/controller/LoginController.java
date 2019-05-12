package com.mss.framework.base.user.oauth.controller;

import com.mss.framework.base.user.oauth.common.Constants;
import com.mss.framework.base.user.oauth.model.AuthorizationResponse;
import com.mss.framework.base.user.oauth.model.User;
import com.mss.framework.base.user.oauth.util.EncryptUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

/**
 * @Description: 登录
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 22:54
 */
@RestController
public class LoginController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${own.oauth2.client-id}")
    private String clientId;

    @Value("${own.oauth2.scope}")
    private String scope;

    @Value("${own.oauth2.client-secret}")
    private String clientSecret;

    @Value("${own.oauth2.user-authorization-uri}")
    private String authorizationUri;

    @Value("${own.oauth2.access-token-uri}")
    private String accessTokenUri;

    @Value("${own.oauth2.resource.userInfoUri}")
    private String userInfoUri;

    /**
     * 登录验证（实际登录调用认证服务器）
     *
     * @param request HttpServletRequest
     * @return org.springframework.web.servlet.ModelAndView
     * @author zifangsky
     * @date 2018/7/25 16:42
     * @since 1.0.0
     */
    @RequestMapping("/login/other")
    public ModelAndView login(HttpServletRequest request) {
        //当前系统登录成功之后的回调URL
        String redirectUrl = request.getParameter("redirectUrl");
        //当前系统请求认证服务器成功之后返回的Authorization Code
        String code = request.getParameter("code");

        //最后重定向的URL
        String resultUrl = "redirect:";
        HttpSession session = request.getSession();
        //当前请求路径
        String currentUrl = request.getRequestURL().toString();

        //code为空，则说明当前请求不是认证服务器的回调请求，则重定向URL到认证服务器登录
        if (StringUtils.isBlank(code)) {
            //如果存在回调URL，则将这个URL添加到session
            if (StringUtils.isNoneBlank(redirectUrl)) {
                session.setAttribute(Constants.SESSION_LOGIN_REDIRECT_URL, redirectUrl);
            }
            //生成随机的状态码，用于防止CSRF攻击
            String status = EncryptUtils.getRandomStr1(6);
            session.setAttribute(Constants.SESSION_AUTH_CODE_STATUS, status);
            //拼装请求Authorization Code的地址
            resultUrl += MessageFormat.format(authorizationUri, clientId, status, currentUrl);
        } else {
            //2. 通过Authorization Code获取Access Token
            AuthorizationResponse response = restTemplate.getForObject(accessTokenUri, AuthorizationResponse.class, clientId, clientSecret, code, currentUrl);
            //如果正常返回
            if (response != null && StringUtils.isNotBlank(response.getAccess_token())) {
                System.out.println(response);

                //2.1 将Access Token存到session
                session.setAttribute(Constants.SESSION_ACCESS_TOKEN, response.getAccess_token());

                //2.2 再次查询用户基础信息，并将用户ID存到session
                User user = restTemplate.getForObject(userInfoUri, User.class, response.getAccess_token());
                if (user != null && StringUtils.isNotBlank(user.getUsername())) {
                    session.setAttribute(Constants.SESSION_USER, user);
                }
            }

            //3. 从session中获取回调URL，并返回
            redirectUrl = (String) session.getAttribute(Constants.SESSION_LOGIN_REDIRECT_URL);
            session.removeAttribute("redirectUrl");
            if (StringUtils.isNotBlank(redirectUrl)) {
                resultUrl += redirectUrl;
            } else {
                resultUrl += "/user/userIndex";
            }
        }

        return new ModelAndView(resultUrl);
    }
}
