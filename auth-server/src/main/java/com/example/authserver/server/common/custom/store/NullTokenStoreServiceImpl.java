package com.example.authserver.server.common.custom.store;

import org.springframework.security.core.Authentication;

/**
 * @author: 长安
 */
public class NullTokenStoreServiceImpl implements TokenStoreService<Authentication, String> {
    @Override
    public String setToken(Authentication authentication, String token) {
        return null;
    }

    @Override
    public boolean hasToken(String token) {
        return false;
    }

    @Override
    public boolean hasUser(String username) {
        return false;
    }

    @Override
    public Authentication getTokenAuthentication(String token) {
        return null;
    }

    @Override
    public Authentication getUserAuthentication(String username) {
        return null;
    }

    @Override
    public Authentication evictToken(String username) {
        return null;
    }

    @Override
    public Authentication evictUser(String token) {
        return null;
    }
}
