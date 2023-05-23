package com.example.authserver.server.auth.custom;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author: 长安
 */
public class SecurityContextFromHeaderTokenFilter extends OncePerRequestFilter {

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy();


    final TokenParser tokenParser;
    final UserDetailsService userDetailsService;

    public SecurityContextFromHeaderTokenFilter(TokenParser tokenParser, UserDetailsService userDetailsService) {
        this.tokenParser = tokenParser;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info(LogMessage.format("打印请求日志: %s - %s", request.getMethod(), request.getHeader(HttpHeaders.AUTHORIZATION)));

        String token = findHeaderToken(request);
        if(StringUtils.isBlank(token)) {
            filterChain.doFilter(request, response);
            return ;
        }

        try {
            Authentication authenticationResult = tokenParser.decode(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationResult.getName());
            Authentication authenticated = UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());
            SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authenticated);
            this.securityContextHolderStrategy.setContext(context);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s. --c", authenticationResult));
            }
        } catch (InvalidBearerTokenException ex) {
            logger.warn("检测请求头token失败。 --c", ex);
        } catch (Exception ex) {
            logger.warn("检测请求头token失败。 --c", ex);
        }
        filterChain.doFilter(request, response);
    }

    private String findHeaderToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.isBlank(token) || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring("Bearer ".length());
    }
}
