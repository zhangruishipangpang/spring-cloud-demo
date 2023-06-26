package com.example.authserver.server.common.custom;

import com.example.authserver.server.common.custom.convert.AbstractAuthenticationConverter;
import com.example.authserver.server.common.custom.convert.DefaultAuthenticationConverter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author: 长安
 */
@Slf4j
public class UserAuthenticationFilter extends AbstractAuthenticationProcessingFilter implements Ordered {

    public static final String DEFAULT_LOGIN_PATH = "/auth/login";
    public static final String USER_AUTH_HEADER_KEY = "A_SPID"; // auth service permit id

    private final String loginPath = DEFAULT_LOGIN_PATH;

    @Setter @Getter private AuthenticationConverter authenticationConverter = new DelegatingAuthenticationConverter(Arrays.asList(new DefaultAuthenticationConverter()));

    public UserAuthenticationFilter(boolean placeholder) {
        super(DEFAULT_LOGIN_PATH);
        setRequiresAuthenticationRequestMatcher(requestMatcher(placeholder));
    }

    public UserAuthenticationFilter(AuthenticationManager authenticationManager, boolean placeholder) {
        super(DEFAULT_LOGIN_PATH); // 默认一个初始化的路径后替换
        setRequiresAuthenticationRequestMatcher(requestMatcher(placeholder));
        setAuthenticationManager(authenticationManager);
    }

    private RequestMatcher requestMatcher(boolean placeholder) {
        RequestMatcher headerMatcher = new RequestHeaderRequestMatcher(USER_AUTH_HEADER_KEY);
        RequestMatcher path = new AntPathRequestMatcher(DEFAULT_LOGIN_PATH, HttpMethod.POST.name());

        RequestMatcher placeholderMatcher = new RequestMatcher() {
            @Override
            public boolean matches(HttpServletRequest request) {
                return placeholder;
            }
        };
        return new AndRequestMatcher(headerMatcher, path, placeholderMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        Authentication requestAuthentication = authenticationConverter.convert(request);

        if(log.isDebugEnabled()) {
            log.debug("[DEBUG]UserAuthenticationFilter -> requestAuthentication[{}]", requestAuthentication);
        }

        return getAuthenticationManager().authenticate(requestAuthentication);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
