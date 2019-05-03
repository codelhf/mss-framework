package com.mss.framework.base.user.client.other.support;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specific session store.
 *
 * @author Jerome Leleu
 * @since 1.4.0
 */
public final class ShiroSessionStore implements SessionStore {

    private final static Logger log = LoggerFactory.getLogger(ShiroSessionStore.class);

    @Override
    public String getOrCreateSessionId(WebContext context) {
        return SecurityUtils.getSubject().getSession().getId().toString();
    }

    @Override
    public Object get(WebContext context, String key) {
        return SecurityUtils.getSubject().getSession().getAttribute(key);
    }

    @Override
    public void set(WebContext context, String key, Object value) {
        try {
            SecurityUtils.getSubject().getSession().setAttribute(key, value);
        } catch (final UnavailableSecurityManagerException e) {
            log.warn("Should happen just once at startup in some specific case of Shiro Spring configuration", e);
        }
    }
}
