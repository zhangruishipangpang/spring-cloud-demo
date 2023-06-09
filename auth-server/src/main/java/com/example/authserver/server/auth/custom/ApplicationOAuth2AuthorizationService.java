package com.example.authserver.server.auth.custom;

import com.example.authserver.server.common.custom.UserCustomAuthenticationToken;
import com.example.authserver.server.common.custom.store.TokenStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author: 长安
 * TODO 自定义实现可以继承接口实现方法，使用 Redis 缓存。
 */
@Slf4j
//@Service
public class ApplicationOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final OAuth2AuthorizationService delegate = new InMemoryOAuth2AuthorizationService();

    private final TokenStoreService tokenStoreService;

    @SuppressWarnings("rawtypes")
    public ApplicationOAuth2AuthorizationService(@Qualifier("oauth2TokenStoreService") TokenStoreService tokenStoreService) {
        this.tokenStoreService = tokenStoreService;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        delegate.save(authorization);
        if(Objects.isNull(authorization.getAccessToken())) {
            return ;
        }
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        String token = accessToken.getToken().getTokenValue();
        // TODO 这里暂时这么给，需要修改
        UserCustomAuthenticationToken.authenticated(authorization.getPrincipalName(), null, null);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        delegate.remove(authorization);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        return delegate.findById(id);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        return delegate.findByToken(token, tokenType);
    }
}
