package com.example.authserver.server.common;

import com.example.authserver.server.auth.custom.JwtTokenAuthenticationSuccessHandler;
import com.example.authserver.server.auth.custom.SecurityContextFromHeaderTokenFilter;
import com.example.authserver.server.auth.custom.token.DefaultTokenParser;
import com.example.authserver.server.common.custom.CustomLoginConfigurer;
import com.example.authserver.server.common.custom.UserAuthenticationFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

/**
 * @author 长安
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfig {

    final JwtDecoder jwtDecoder;
    final JwtEncoder jwtEncoder;
    final UserDetailsService userDetailsService;
    final AuthenticationManager authenticationManager;

    public DefaultSecurityConfig(JwtDecoder jwtDecoder, JwtEncoder jwtEncoder, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.jwtDecoder = jwtDecoder;
        this.jwtEncoder = jwtEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }


    @Bean
    @Order(200)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors().and()
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("/auth/**").hasRole("USER")
                    .requestMatchers("/unAuth/**", "/login",
                        "/favicon.ico", "/error", "/verification_code/**").permitAll()
                    .requestMatchers("/").authenticated()
                    .anyRequest().authenticated()
            )
            .headers().frameOptions().sameOrigin()
            .and()
            .csrf().disable()
            .cors().disable()
            .formLogin().disable()
//            .successHandler(authenticationSuccessHandler())
//            .usernameParameter("un")
//            .passwordParameter("pw")
//            .and()
            .addFilterAt(securityContextFromHeaderTokenFilter(), SecurityContextHolderFilter.class)
            .securityContext().disable() // 去除Session 处理，使用 JWT Token 方式认证
            .requestCache().disable()
            .rememberMe().disable()
            .authenticationManager(authenticationManager)
        ;

        http.addFilterAt(new UserAuthenticationFilter(false), UsernamePasswordAuthenticationFilter.class);

        http.apply(new CustomLoginConfigurer<>())
                .successHandler(authenticationSuccessHandler())
                .loginProcessingUrl(UserAuthenticationFilter.DEFAULT_LOGIN_PATH)
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        log.info("[ERROR]login failure, error msg -> {}", exception.getMessage());
                        log.error("[ERROR]login failure, error msg -> ", exception);

                        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
                        httpResponse.setStatusCode(HttpStatusCode.valueOf(500));
                        httpResponse.getBody().write(exception.getMessage().getBytes());
                        httpResponse.getBody().flush();
                    }
                })
        ;


        return http.build();
    }
    // @formatter:on

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new JwtTokenAuthenticationSuccessHandler(new DefaultTokenParser(jwtEncoder, jwtDecoder));
    }

    private Filter securityContextFromHeaderTokenFilter() {
        return new SecurityContextFromHeaderTokenFilter(new DefaultTokenParser(jwtEncoder, jwtDecoder), userDetailsService);
    }


}