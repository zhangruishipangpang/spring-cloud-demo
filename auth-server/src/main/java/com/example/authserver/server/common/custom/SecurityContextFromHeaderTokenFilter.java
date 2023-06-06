package com.example.authserver.server.common.custom;

import com.example.authserver.server.auth.custom.TokenParser;
import com.example.authserver.server.common.custom.convert.TokenAuthenticationConvert;
import com.example.authserver.server.common.custom.store.NullTokenStoreServiceImpl;
import com.example.authserver.server.common.custom.store.TokenStoreService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author: 长安
 */
@SuppressWarnings("rawtypes")
public class SecurityContextFromHeaderTokenFilter extends OncePerRequestFilter {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy();


    private final TokenParser tokenParser;
    private final UserDetailsService userDetailsService;
    private final AuthenticationConverter tokenAuthenticationConverter;

    public SecurityContextFromHeaderTokenFilter(TokenParser tokenParser, UserDetailsService userDetailsService) {
        this.tokenParser = tokenParser;
        this.userDetailsService = userDetailsService;
        tokenAuthenticationConverter = new TokenAuthenticationConvert(tokenParser, new NullTokenStoreServiceImpl());
    }

    public SecurityContextFromHeaderTokenFilter(TokenParser tokenParser, UserDetailsService userDetailsService, TokenStoreService tokenStoreService) {
        this.tokenParser = tokenParser;
        this.userDetailsService = userDetailsService;
        tokenAuthenticationConverter = new TokenAuthenticationConvert(tokenParser, tokenStoreService);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info(LogMessage.format("打印请求日志: %s - %s", request.getMethod(), request.getHeader(HttpHeaders.AUTHORIZATION)));

        if(!requireParseToken(request)) {
            filterChain.doFilter(request, response);
            return ;
        }

        try {
            Authentication authenticationResult = tokenAuthenticationConverter.convert(request);
            if(logger.isDebugEnabled()) {
                logger.debug(String.format("[DEBUG]SecurityContextFromHeaderTokenFilter find authentication user -> [%s]", authenticationResult.toString()));
            }
            SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authenticationResult);
            this.securityContextHolderStrategy.setContext(context);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s. --c", authenticationResult));
            }
        } catch (InvalidBearerTokenException ex) {
            logger.warn("检测请求头token失败。 --c", ex);
        } catch (Exception ex) {
            logger.warn("解析token失败。 --c", ex);
        }
        filterChain.doFilter(request, response);
    }

    private boolean requireParseToken(HttpServletRequest request) {
        return StringUtils.isNotBlank(findHeaderToken(request));
    }

    private String findHeaderToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.isBlank(token) || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring("Bearer ".length());
    }
}
