package com.example.authserver.server.common.custom.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author: 长安
 * 手机号验证码登录Token
 */
public class MobileAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public MobileAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    /**
     *
     * @param principal     手机号
     * @param credentials   短信验证码
     * @param authorities   权限
     */
    public MobileAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
