package com.example.authserver.server.resource;

import com.example.authserver.server.auth.custom.SecurityContextFromHeaderTokenFilter;
import com.example.authserver.server.auth.custom.token.DefaultTokenParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

/**
 * @author 长安
 */
@Slf4j
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class ResourceConfig {
    JwtDecoder jwtDecoder;

    JwtEncoder jwtEncoder;

    UserDetailsService userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtEncoder(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Autowired
    public void setJwtDecoder(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Bean
    @Order(-100)
    SecurityFilterChain resourceSecurityFilterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        // 第三方接入平台使用该接口获取用户资源
        RequestMatcher requestMatcher = new AntPathRequestMatcher("/resource/**");
        http.securityMatcher(requestMatcher);

        http.authorizeHttpRequests(matcherRegistry -> {
            matcherRegistry.anyRequest().authenticated();
        });

        http.securityContext().disable(); // 关闭 获取用户认证方式，使用 JWT
        http.requestCache().disable();
        http.csrf().disable();
        http.cors().disable();

        http.addFilterAt(new SecurityContextFromHeaderTokenFilter(new DefaultTokenParser(jwtEncoder, jwtDecoder), userDetailsService), SecurityContextHolderFilter.class);

        return http.build();
    }

    @Component
    public static class FailureEvents {

        @EventListener(classes = AuthenticationFailureBadCredentialsEvent.class)
        public void onFailure(AuthenticationFailureBadCredentialsEvent badCredentials) {
            if (badCredentials.getAuthentication() instanceof BearerTokenAuthenticationToken bearerTokenAuthenticationToken) {
                // ... handle
                AuthenticationException ex = badCredentials.getException();
                log.error("check Bearer Token error, exception is -> {}", ex.getMessage());
            }
        }
    }
}
