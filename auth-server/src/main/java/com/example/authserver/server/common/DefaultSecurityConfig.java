package com.example.authserver.server.common;

import com.example.authserver.server.auth.custom.JwtTokenAuthenticationSuccessHandler;
import com.example.authserver.server.auth.custom.SecurityContextFromHeaderTokenFilter;
import com.example.authserver.server.auth.custom.token.DefaultTokenParser;
import com.example.authserver.server.common.custom.CustomLoginConfigurer;
import com.example.authserver.server.common.custom.UserAuthenticationFilter;
import com.example.authserver.server.common.custom.UserAuthenticationProvider;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
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

    public DefaultSecurityConfig(JwtDecoder jwtDecoder, JwtEncoder jwtEncoder, UserDetailsService userDetailsService) {
        this.jwtDecoder = jwtDecoder;
        this.jwtEncoder = jwtEncoder;
        this.userDetailsService = userDetailsService;
    }


    @Bean
    @Order(200)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors().and()
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("/auth/**").hasRole("USER")
                    .requestMatchers("/unAuth/**", "/login", "/favicon.ico", "/error").permitAll()
                    .requestMatchers(
                        new AntPathRequestMatcher("/h2/**", HttpMethod.GET.name()),
                        new AntPathRequestMatcher("/h2/**", HttpMethod.POST.name())
                    ).permitAll()
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
            .authenticationManager(authenticationManager())
        ;

        http.addFilterAt(new UserAuthenticationFilter(false), UsernamePasswordAuthenticationFilter.class);

        http.apply(new CustomLoginConfigurer<>())
                .successHandler(authenticationSuccessHandler())
            .loginProcessingUrl(UserAuthenticationFilter.DEFAULT_LOGIN_PATH)
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

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthenticationManager authenticationManager() {

        return new ProviderManager(List.of(
            new UserAuthenticationProvider(userDetailsService, passwordEncoder())
        ));
    }
}