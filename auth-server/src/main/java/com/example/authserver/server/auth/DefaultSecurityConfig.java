package com.example.authserver.server.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

/**
 * @author 长安
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfig {

    JwtDecoder jwtDecoder;

    @Autowired
    public void setJwtDecoder(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    // @formatter:off
    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") HttpSecurity http) throws Exception {

        AuthenticationManager sharedObject = http.getSharedObject(AuthenticationManager.class);
        log.info("http share object -> {}", sharedObject);

        http
            .cors().and()
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("/auth/**").hasRole("USER")
                    .requestMatchers("/unAuth/**", "/login", "/favicon.ico").permitAll()
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
            .formLogin()
            .usernameParameter("un")
            .passwordParameter("pw")
//            .successForwardUrl("/")
//            .failureForwardUrl("/login")
            .and()
            .addFilterAfter(new BearerTokenAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        ;
        DefaultSecurityFilterChain build = http.build();

        AuthenticationManager sharedObject1 = http.getSharedObject(AuthenticationManager.class);
        log.info("http share object -> {}", sharedObject1);

        return build;
    }
    // @formatter:on


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(new JwtAuthenticationProvider(jwtDecoder)));
    }

}