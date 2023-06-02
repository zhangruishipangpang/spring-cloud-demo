package com.example.authserver.server.common.custom;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Objects;

/**
 * @author: 长安
 */
public class CustomLoginConfigurer<H extends HttpSecurityBuilder<H>>  extends
    AbstractAuthenticationFilterConfigurer<H, CustomLoginConfigurer<H>, UserAuthenticationFilter> {

    public CustomLoginConfigurer() {
        // default authentication filter init
        super(new UserAuthenticationFilter(true), null);
    }

    /**
     *
     * @param filter authentication filter {@link AbstractAuthenticationProcessingFilter }
     */
    public void authFilter(UserAuthenticationFilter filter) {
        setAuthenticationFilter(Objects.requireNonNull(filter, "filter is null"));
    }

    public CustomLoginConfigurer<H> authenticationConverter(AuthenticationConverter authenticationConverter) {
        getAuthenticationFilter().setAuthenticationConverter(authenticationConverter);
        return this;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }

    @Override
    public void init(H http) throws Exception {
        super.init(http);
    }
}
