package com.mss.framework.base.user.social.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.mss.framework.base.user.social.api.QQApi20;
import com.mss.framework.base.user.social.definition.QQAttributesDefinition;
import com.mss.framework.base.user.social.profile.QQProfile;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpCommunicationException;
import org.pac4j.oauth.client.BaseOAuth20Client;
import org.pac4j.oauth.profile.JsonHelper;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;
import org.scribe.oauth.ProxyOAuth20ServiceImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * qq登录client
 *
 * @author zrk
 * @date 2016年4月15日 下午5:42:35
 */
public class QQClient extends BaseOAuth20Client<QQProfile> {

    private final static QQAttributesDefinition QQ_ATTRIBUTES = new QQAttributesDefinition();

    private static Pattern callbackPattern = Pattern.compile("callback\\((.*)\\)");

    private Token accessToken = null;

    public final static String DEFAULT_SCOPE = "get_user_info";

    protected String scope = DEFAULT_SCOPE;


    public QQClient() {
    }

    public QQClient(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected QQClient newClient() {
        final QQClient newClient = new QQClient();
        newClient.setScope(this.scope);
        return newClient;
    }

    @Override
    protected void internalInit(WebContext context) {
        super.internalInit(context);
        QQApi20 api20 = new QQApi20();
        if (StringUtils.isNotBlank(this.scope)) {
            this.service = new ProxyOAuth20ServiceImpl(api20,
                    new OAuthConfig(this.key, this.secret, this.callbackUrl, SignatureType.Header, this.scope, null),
                    this.connectTimeout, this.readTimeout, this.proxyHost, this.proxyPort, true, true);
        } else {
            this.service = new ProxyOAuth20ServiceImpl(api20,
                    new OAuthConfig(this.key, this.secret, this.callbackUrl, SignatureType.Header, null, null),
                    this.connectTimeout, this.readTimeout, this.proxyHost, this.proxyPort, true, true);
        }
    }

    ;

    //认证被用户取消
    @Override
    protected boolean hasBeenCancelled(WebContext context) {
        return false;
    }

    //获取用户信息的URL
    @Override
    protected String getProfileUrl(Token accessToken) {
        this.accessToken = accessToken;
        return "https://graph.qq.com/oauth2.0/me";
    }


    //处理用户信息
    @Override
    protected QQProfile extractUserProfile(String body) {
        Matcher matcher = callbackPattern.matcher(body);
        String opengId = null;
        String clientId = null;
        if (matcher.find()) {
            final JsonNode json = JsonHelper.getFirstNode(matcher.group(1));
            opengId = (String) JsonHelper.get(json, "openid");
            clientId = (String) JsonHelper.get(json, "client_id");
        }
        if (opengId == null || clientId == null)
            throw new OAuthException("接口返回数据miss openid: " + body);

        String get_user_info_url = "https://graph.qq.com/user/get_user_info?openid=%s&oauth_token=%s&oauth_consumer_key=%s";
        final ProxyOAuthRequest request = createProxyRequest(String.format(get_user_info_url, opengId, this.accessToken.getToken(), clientId));
        final Response response = request.send();
        final int code = response.getCode();
        body = response.getBody();
        if (code != 200) {
            throw new HttpCommunicationException(code, body);
        }

        final QQProfile profile = new QQProfile();
        logger.info("========= extractUserProfile Method  body:" + body);
        final JsonNode json = JsonHelper.getFirstNode(body);
        if (null != json) {
            profile.addAttribute(QQAttributesDefinition.OPEN_ID, opengId);
            for (final String attribute : QQ_ATTRIBUTES.getPrincipalAttributes()) {
                Object obj = JsonHelper.get(json, attribute);
                if (obj != null)
                    profile.addAttribute(attribute, obj.toString());
            }
        }
        return profile;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(final String scope) {
        this.scope = scope;
    }


}
