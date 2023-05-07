package com.example.authserver.server.auth;

import com.example.authserver.server.auth.custom.ApplicationUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author Joe Grandja
 * @since 0.1.0
 */
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfig {

    // @formatter:off
    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") HttpSecurity http) throws Exception {
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
        ;
        return http.build();
    }
    // @formatter:on


    // @formatter:off
//    @Bean
//    UserDetailsService users() {
//        UserDetails user = User.withUsername("user1")
//            .password(new BCryptPasswordEncoder().encode("123456"))
//            .roles("USER")
//            .build();
//        return new InMemoryUserDetailsManager(user);
//    }
    // @formatter:on

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    SessionRegistry sessionRegistry() {
//        return new SessionRegistryImpl();
//    }
//
//    @Bean
//    HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }

//    @Bean
//    RequestCache requestCache() {
//        return new NullRequestCache();
//    }

}