package com.example.authserver.server.auth.custom;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * @author: 长安
 * 用户登录成功返回 token
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class JwtTokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();

    final TokenParser tokenParser;

    public JwtTokenAuthenticationSuccessHandler(TokenParser tokenParser) {
        this.tokenParser = tokenParser;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Jwt jwt = generalToken(authentication);
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
        Jwt jwt = (Jwt) tokenAdapter.get(Jwt.class);
        log.info("用户生成token -> {}", jwt);
        return jwt;
    }
}
