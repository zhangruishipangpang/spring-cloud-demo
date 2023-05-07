package com.example.testsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.util.StringUtils;

/**
 * @Auther: 长安
 */
@Configuration
public class SecurityConfig {

    // @formatter:off
    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("/auth/**").hasRole("USER")
                    .requestMatchers("/unAuth/**").permitAll()
                    .anyRequest().authenticated()
            )
            .csrf().disable()
            .formLogin()
            .usernameParameter("un")
            .passwordParameter("pw")
            .successForwardUrl("/html")
            .failureForwardUrl("/login")
        ;
        return http.build();
    }

    // @formatter:off
    @Bean
    UserDetailsService users() {
        UserDetails user = User.withUsername("user1")
            .password(new BCryptPasswordEncoder().encode("123456"))
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
    // @formatter:on

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    RequestCache requestCache() {
        return new NullRequestCache();
    }

}
