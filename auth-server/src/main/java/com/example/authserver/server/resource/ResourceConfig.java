package com.example.authserver.server.resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

/**
 * @Auther: 长安
 */
@Slf4j
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class ResourceConfig {


    @Bean
    @Order(-100)
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    SecurityFilterChain resourceSecurityFilterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        RequestMatcher requestMatcher = new AntPathRequestMatcher("/resource/**");
        http.securityMatcher(requestMatcher);

        http.authorizeHttpRequests(matcherRegistry -> {
            matcherRegistry.anyRequest().authenticated();
        });
        return http.build();
    }

    @Component
    public class FailureEvents {
        @EventListener
        public void onFailure(AuthenticationFailureBadCredentialsEvent badCredentials) {
            if (badCredentials.getAuthentication() instanceof BearerTokenAuthenticationToken bearerTokenAuthenticationToken) {
                // ... handle
                AuthenticationException ex = badCredentials.getException();
                log.error("check Bearer Token error, exception is -> ", ex.getMessage());
            }
        }
    }
}
