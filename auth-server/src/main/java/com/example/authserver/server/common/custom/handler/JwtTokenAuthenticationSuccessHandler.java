package com.example.authserver.server.common.custom.handler;

import com.example.authserver.server.auth.custom.TokenParser;
import com.example.authserver.server.auth.custom.UserTokenAdapter;
import com.example.authserver.server.common.custom.ex.TokenException;
import com.example.authserver.server.common.custom.store.TokenStoreService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Objects;

/**
 * @author: 长安
 * 用户登录成功返回 token
 */
@Slf4j
public class JwtTokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();

    private final TokenParser<Authentication> tokenParser;
    private final TokenStoreService tokenStoreService;

    public JwtTokenAuthenticationSuccessHandler(TokenParser<Authentication> tokenParser, TokenStoreService tokenStoreService) {
        this.tokenParser = tokenParser;
        this.tokenStoreService = tokenStoreService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Jwt jwt = generalToken(authentication);
        if(Objects.isNull(jwt)) {
            throw new TokenException("general token error");
        }
        tokenStoreService.setToken(authentication, jwt.getTokenValue());
        // 用户认证返回的认证信息
        OAuth2AccessTokenResponse oAuth2AccessTokenResponse = OAuth2AccessTokenResponse.withToken(jwt.getTokenValue())
            .tokenType(OAuth2AccessToken.TokenType.BEARER)
            .expiresIn(jwt.getExpiresAt().getEpochSecond())
            .build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        accessTokenHttpResponseConverter.write(oAuth2AccessTokenResponse, null, httpResponse);


    }

    private Jwt generalToken(Authentication authentication) {
        UserTokenAdapter tokenAdapter = tokenParser.encode(authentication);
        return (Jwt) tokenAdapter.get(Jwt.class);
    }
}
