package com.example.authserver.server.common.custom.store;

import com.example.authserver.server.common.custom.ex.TokenException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: 长安
 */
public class TokenStoreServiceImpl implements TokenStoreService{

    private final Map<String/*token*/, Authentication> tokenStore = new ConcurrentHashMap<>();
    private final Map<String/*username*/, String/*token*/> userTokenStore = new ConcurrentHashMap<>();

    @Override
    public String setToken(Authentication authentication, String token) {
        this.checkToken(token);
        this.checkAuthentication(authentication);
        this.checkUsername(authentication);

        String username = authentication.getName();
        this.tokenStore.put(token, authentication);
        this.userTokenStore.put(username, token);
        return username;
    }

    @Override
    public boolean hasToken(String token) {
        this.checkToken(token);
        return this.tokenStore.containsKey(token);
    }

    @Override
    public boolean hasUser(String username) {
        this.checkUsername(username);
        return this.userTokenStore.containsKey(username);
    }

    @Override
    public Authentication getTokenAuthentication(String token) {
        return hasToken(token) ? this.tokenStore.get(token) : null;
    }

    @Override
    public Authentication getUserAuthentication(String username) {
        String token = hasUser(username) ? this.userTokenStore.get(username) : null;
        return getTokenAuthentication(token);
    }

    @Override
    public Authentication evictToken(String username) {
        this.checkUsername(username);

        String token = hasUser(username) ? this.userTokenStore.get(username) : null;
        if(StringUtils.isBlank(token)) {
            return null;
        }
        this.userTokenStore.remove(username);
        return this.tokenStore.remove(token);
    }

    @Override
    public Authentication evictUser(String token) {
        this.checkToken(token);

        Authentication removed = this.tokenStore.remove(token);
        if(Objects.isNull(removed)) {
            return null;
        }
        String removedToken = hasUser(removed.getName()) ? this.userTokenStore.remove(removed.getName()) : null;
        return removed;
    }

    private void checkToken(String token) {
        if(StringUtils.isBlank(token)) {
            throw new TokenException("token is blank");
        }
    }

    private void checkUsername(String username) {
        if(StringUtils.isBlank(username)) {
            throw new TokenException("username is blank");
        }
    }

    private void checkAuthentication(Authentication authentication) {
        if(Objects.isNull(authentication)) {
            throw new TokenException("authentication is null");
        }
    }

    private void checkUsername(Authentication authentication) {
        if(StringUtils.isBlank(authentication.getName())) {
            throw new TokenException("username is blank");
        }
    }
}
