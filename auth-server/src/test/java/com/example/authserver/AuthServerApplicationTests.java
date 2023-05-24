package com.example.authserver;

import com.example.authserver.general.entity.Oauth2RegisteredClientEntity;
import com.example.authserver.general.repository.Oauth2RegisteredClientEntityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AuthServerApplicationTests {

    @Resource
    Oauth2RegisteredClientEntityRepository oauth2RegisteredClientEntityRepository;

    @Resource
    JdbcRegisteredClientRepository jdbcRegisteredClientRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void init_selectClientAll() {

        List<Oauth2RegisteredClientEntity> all = oauth2RegisteredClientEntityRepository.findAll();
        Assertions.assertTrue(all != null);
        log.info("all is [{}]", all);
    }

    @Test
    void init_insertClient() throws JsonProcessingException {

        Oauth2RegisteredClientEntity entity = new Oauth2RegisteredClientEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setClientId("messaging-client");
        entity.setClientSecret(new BCryptPasswordEncoder().encode("secret"));
        entity.setClientAuthenticationMethods(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue());
        entity.setAuthorizationGrantTypes(
            new StringJoiner(",")
                .add(AuthorizationGrantType.AUTHORIZATION_CODE.getValue())
                .add(AuthorizationGrantType.REFRESH_TOKEN.getValue())
                .add(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue())
                .toString()
        );
        entity.setRedirectUris(
            new StringJoiner(",")
                .add("http://127.0.0.1:8080/authorized")
                .add("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
                .toString()
        );
        entity.setScopes(
            new StringJoiner(",")
                .add(OidcScopes.OPENID)
                .add(OidcScopes.PROFILE)
                .add("message.read")
                .add("message.write")
                .toString()
        );
        entity.setClientSettings(
            new ObjectMapper().writeValueAsString(ClientSettings.builder().requireAuthorizationConsent(true).build())
        );
        entity.setClientIdIssuedAt(new Timestamp(Instant.now().toEpochMilli()));
        entity.setClientSecretExpiresAt(new Timestamp(Instant.now().plus(100, ChronoUnit.DAYS).toEpochMilli()));
        entity.setClientName("第一个测试的客户端配置");
        entity.setTokenSettings(" - ");

        log.info("will be saving client is -> {}", entity.toString());

        Oauth2RegisteredClientEntity save = oauth2RegisteredClientEntityRepository.save(entity);

        assertNotNull(save);

        log.info("new client is -> {}", save);
    }

    @Test
    void test_searchJdbcClient() {
        RegisteredClient mess = jdbcRegisteredClientRepository.findByClientId("messaging-client2");
        log.info("client message is -> {}", mess);
    }

    @Test
    void test_insertJdbcClient() {
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("messaging-client2")
            .clientSecret(new BCryptPasswordEncoder().encode("secret"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantTypes(authorizationGrantTypes -> {
                authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                authorizationGrantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                authorizationGrantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
            })
            .redirectUris(urls -> {
                urls.add("http://127.0.0.1:8080/authorized");
                urls.add("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc");
            })
            .scopes(scopeSet -> {
                scopeSet.add(OidcScopes.PROFILE);
                scopeSet.add(OidcScopes.OPENID);
                scopeSet.add("message.read");
                scopeSet.add("message.write");
            })
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .clientIdIssuedAt(Instant.now())
            .clientSecretExpiresAt(Instant.now().plus(100, ChronoUnit.DAYS))
            .clientName("测试客户端配置")
            .tokenSettings(TokenSettings.builder().build())
            .build();

        jdbcRegisteredClientRepository.save(client);
    }

    @Test
    void test_updateClient() {

        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder();
        tokenSettingsBuilder.accessTokenTimeToLive(Duration.of(3, ChronoUnit.HOURS));
//        tokenSettingsBuilder.idTokenSignatureAlgorithm() 配置签名算法
        tokenSettingsBuilder.reuseRefreshTokens(true); // 重用
        tokenSettingsBuilder.refreshTokenTimeToLive(Duration.of(1, ChronoUnit.DAYS));
        tokenSettingsBuilder.authorizationCodeTimeToLive(Duration.of(20, ChronoUnit.MINUTES));


        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("messaging-client2")
            .clientSecret(new BCryptPasswordEncoder().encode("secret"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantTypes(authorizationGrantTypes -> {
                authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                authorizationGrantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                authorizationGrantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
            })
            .redirectUris(urls -> {
                urls.add("http://127.0.0.1:8080/authorized");
                urls.add("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc");
            })
            .scopes(scopeSet -> {
                scopeSet.add(OidcScopes.PROFILE);
                scopeSet.add(OidcScopes.OPENID);
                scopeSet.add("message.read");
                scopeSet.add("message.write");
            })
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
            .clientIdIssuedAt(Instant.now())
            .clientSecretExpiresAt(Instant.now().plus(100, ChronoUnit.DAYS))
            .clientName("测试客户端配置")
            .tokenSettings(tokenSettingsBuilder.build())
            .build();

        jdbcRegisteredClientRepository.save(client);
    }

}
