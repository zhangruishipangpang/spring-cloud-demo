package com.example.authserver.server.common;

import com.example.authserver.server.auth.custom.token.DefaultTokenParser;
import com.example.authserver.server.common.custom.SecurityContextFromHeaderTokenFilter;
import com.example.authserver.server.common.custom.provider.UserAuthenticationProvider;
import com.example.authserver.server.common.custom.store.TokenStoreService;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 长安
 */
@Configuration
public class CommonSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(List<UserAuthenticationProvider> providers, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        List<AuthenticationProvider> providerList = new ArrayList<>(providers);
        providerList.add(new UserAuthenticationProvider(userDetailsService, passwordEncoder));
        AnnotationAwareOrderComparator.sort(providerList);
        return new ProviderManager(providerList);
    }

}
