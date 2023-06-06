package com.example.authserver.server.auth;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import com.example.authserver.server.common.custom.SecurityContextFromHeaderTokenFilter;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationConsentAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.context.SecurityContextHolderFilter;

/**
 * @Auther: 长安
 */
@Slf4j
@Configuration
public class AuthorizationServerConfiguration2 {

    final SecurityContextFromHeaderTokenFilter securityContextFromHeaderTokenFilter;

    public AuthorizationServerConfiguration2(SecurityContextFromHeaderTokenFilter securityContextFromHeaderTokenFilter) {
        this.securityContextFromHeaderTokenFilter = securityContextFromHeaderTokenFilter;
    }


    /**
     * 配置 oauth2 相关接口的拦截执行链， 具体的接口配置在 AuthorizationServerSettings 中。
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
        throws Exception {
        // 这里配置了当前 FilterChain 只会拦截oauth2相关的请求
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .tokenEndpoint(oAuth2TokenEndpointConfigurer -> {
//                oAuth2TokenEndpointConfigurer.accessTokenResponseHandler(null);
            })
            .authorizationEndpoint(oAuth2AuthorizationEndpointConfigurer -> {
                oAuth2AuthorizationEndpointConfigurer.authenticationProviders(authenticationProvidersConsumer());
//                oAuth2AuthorizationEndpointConfigurer.errorResponseHandler(authenticationFailureHandler());
            })
            /*
                认证后token存储配置，默认在内存，可配置Redis与DB
                TODO 需要改造这里使其可以将token信息存储到缓存中
             */
            .authorizationService(new InMemoryOAuth2AuthorizationService())
            .oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
        ;

        http
            // Redirect to the login page when not authenticated from the
            // authorization endpoint
            .exceptionHandling((exceptions) -> exceptions
                .authenticationEntryPoint(
                    new LoginUrlAuthenticationEntryPoint("/login"))
            )
            .securityContext().disable()
            .csrf().disable()
            .cors().disable()
            // 添加一个 token 处理器
            .addFilterAt(securityContextFromHeaderTokenFilter , SecurityContextHolderFilter.class)
        ;
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(@Qualifier("customJdbcTemplate") JdbcTemplate jdbcTemplate) {

        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Contract(pure = true)
    private @NotNull Consumer<List<AuthenticationProvider>> authenticationProvidersConsumer() {
        return (authenticationProviders) -> {
            for (AuthenticationProvider authenticationProvider : authenticationProviders) {
                log.info(" consumer authenticationProvider , current -> {} ", authenticationProvider.getClass().getName());
                if(authenticationProvider instanceof OAuth2AuthorizationCodeRequestAuthenticationProvider oAuth2AuthorizationCodeRequestAuthenticationProvider) {

                    oAuth2AuthorizationCodeRequestAuthenticationProvider.setAuthorizationCodeGenerator(authorizationCodeGenerator());
                } else if(authenticationProvider instanceof OAuth2AuthorizationConsentAuthenticationProvider oAuth2AuthorizationConsentAuthenticationProvider) {

                    oAuth2AuthorizationConsentAuthenticationProvider.setAuthorizationCodeGenerator(authorizationCodeGenerator());
                }
            }
        };
    }

    /**
     * 配置自定义的授权码 authorization_code，默认实现：OAuth2AuthorizationCodeGenerator
     *      - 下面简单配置成为了 UUID 的形式。
     */
    @Contract(pure = true)
    private @NotNull OAuth2TokenGenerator<OAuth2AuthorizationCode> authorizationCodeGenerator() {
        return context -> {
            log.info(" use custom authorization_code creator ");
            if (context.getTokenType() == null ||
                !OAuth2ParameterNames.CODE.equals(context.getTokenType().getValue())) {
                return null;
            }
            String authorizationCode = UUID.randomUUID().toString().replaceAll("-", "");
            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plus(context.getRegisteredClient().getTokenSettings().getAuthorizationCodeTimeToLive());
            return new OAuth2AuthorizationCode(authorizationCode, issuedAt, expiresAt);
        };
    }

//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//            .clientId("messaging-client")
//            .clientSecret(new BCryptPasswordEncoder().encode("secret"))
//            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//            .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
//            .redirectUri("http://127.0.0.1:8080/authorized")
//            .scope(OidcScopes.OPENID)
//            .scope(OidcScopes.PROFILE)
//            .scope("message.read")
//            .scope("message.write")
//            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//            .build();
//
//        return new InMemoryRegisteredClientRepository(registeredClient);
//    }

}
