package com.example.authserver.server.common;

import com.example.authserver.server.common.custom.convert.AbstractAuthenticationConverter;
import com.example.authserver.server.common.custom.convert.DefaultAuthenticationConverter;
import com.example.authserver.server.common.custom.convert.MobileAuthenticationConverter;
import com.example.authserver.server.common.custom.handler.JwtTokenAuthenticationSuccessHandler;
import com.example.authserver.server.common.custom.SecurityContextFromHeaderTokenFilter;
import com.example.authserver.server.auth.custom.token.DefaultTokenParser;
import com.example.authserver.server.common.custom.CustomLoginConfigurer;
import com.example.authserver.server.common.custom.UserAuthenticationFilter;
import com.example.authserver.server.common.custom.handler.UserCustomAuthenticationFailureHandler;
import com.example.authserver.server.common.custom.store.TokenStoreService;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;

import java.util.Arrays;
import java.util.List;

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
    final TokenStoreService<Authentication, String> tokenStoreService;

    public DefaultSecurityConfig(JwtDecoder jwtDecoder, JwtEncoder jwtEncoder, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, TokenStoreService tokenStoreService) {
        this.jwtDecoder = jwtDecoder;
        this.jwtEncoder = jwtEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenStoreService = tokenStoreService;
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
                        "/favicon.ico", "/error", "/verification_code/**",
                        "/register/**").permitAll()
                    .requestMatchers("/").authenticated()
                    .anyRequest().authenticated()
            )
            .headers().frameOptions().sameOrigin()
            .and()
            .csrf().disable()
            .cors().disable()
            .formLogin().disable()
            .securityContext().disable() // 去除Session 处理，使用 JWT Token 方式认证
            .requestCache().disable()
            .rememberMe().disable()
            .authenticationManager(authenticationManager)
            .addFilterAt(securityContextFromHeaderTokenFilter(), SecurityContextHolderFilter.class)
        ;

        // 这里用来添加Security拦截器排序占位符
        http.addFilterAt(new UserAuthenticationFilter(false), UsernamePasswordAuthenticationFilter.class);

        List<AuthenticationConverter> converters = Arrays.asList(
            new DefaultAuthenticationConverter(),
            new MobileAuthenticationConverter()
        );

        AnnotationAwareOrderComparator.sort(converters);

        http.apply(new CustomLoginConfigurer<>())
                .successHandler(authenticationSuccessHandler())
                .loginProcessingUrl(UserAuthenticationFilter.DEFAULT_LOGIN_PATH)
                .failureHandler(new UserCustomAuthenticationFailureHandler())
                .authenticationConverter(new DelegatingAuthenticationConverter(converters))
        ;


        return http.build();
    }
    // @formatter:on

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new JwtTokenAuthenticationSuccessHandler(new DefaultTokenParser(jwtEncoder, jwtDecoder), tokenStoreService);
    }

    private Filter securityContextFromHeaderTokenFilter() {
        return new SecurityContextFromHeaderTokenFilter(new DefaultTokenParser(jwtEncoder, jwtDecoder), userDetailsService, tokenStoreService);
    }


}