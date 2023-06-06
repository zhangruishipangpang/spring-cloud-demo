package com.example.authserver.server.common;

import com.example.authserver.server.auth.custom.JwtKeyProperties;
import com.example.authserver.server.common.custom.SecurityContextFromHeaderTokenFilter;
import com.example.authserver.server.auth.custom.token.DefaultTokenParser;
import com.example.authserver.server.common.custom.store.TokenStoreService;
import com.example.authserver.server.common.custom.store.TokenStoreServiceImpl;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: 长安
 */
@Slf4j
@Configuration
public class ApplicationTokenConfiguration {


    @Bean
    public TokenStoreService tokenStoreService() {
        return new TokenStoreServiceImpl();
    }

    /**
     * oauth2 三方集成使用的token存储，与内部使用分开
     * @return oauth2TokenStoreService
     */
    @Bean(name = "oauth2TokenStoreService")
    public TokenStoreService oauth2TokenStoreService() {
        return new TokenStoreServiceImpl();
    }

    @Configuration
    public static class TokenHandlerConfiguration {

        final JwtEncoder jwtEncoder;
        final JwtDecoder jwtDecoder;
        final UserDetailsService userDetailsService;

        public TokenHandlerConfiguration(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, UserDetailsService userDetailsService) {
            this.jwtEncoder = jwtEncoder;
            this.jwtDecoder = jwtDecoder;
            this.userDetailsService = userDetailsService;
        }

        @Bean
        public SecurityContextFromHeaderTokenFilter securityContextFromHeaderTokenFilter() {
            return new SecurityContextFromHeaderTokenFilter(new DefaultTokenParser(jwtEncoder, jwtDecoder), userDetailsService);
        }
    }

    @Configuration
    public static class JwtConfiguration {

        // TODO 暂时使用默认，后续替换成配置文件配置
        JwtKeyProperties jwtKeyProperties = new JwtKeyProperties();

        @Bean
        public JWKSource<SecurityContext> jwkSource() {
            KeyPair keyPair = generateRsaKey();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(jwtKeyProperties.getKeyId())
                .build();
            // 可以配置多个RSAKey 用来使jwt头不单调
            JWKSet jwkSet = new JWKSet(rsaKey);
            return new ImmutableJWKSet<>(jwkSet);
        }

        private static KeyPair generateRsaKey() {
            KeyPair keyPair;
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                keyPair = keyPairGenerator.generateKeyPair();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
            return keyPair;
        }

        @Bean
        public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
            Set<JWSAlgorithm> jwsAlgs = new HashSet<>();
            jwsAlgs.addAll(JWSAlgorithm.Family.RSA);
/*
            jwsAlgs.addAll(JWSAlgorithm.Family.EC);
            jwsAlgs.addAll(JWSAlgorithm.Family.HMAC_SHA);
*/
            ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
            JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(jwsAlgs, jwkSource);
            jwtProcessor.setJWSKeySelector(jwsKeySelector);
            // Override the default Nimbus claims set verifier as NimbusJwtDecoder handles it instead
//            jwtProcessor.setJWTClaimsSetVerifier((claims, context) -> {
//                if(context instanceof SimpleSecurityContext securityContext) {
//                    String customMapKey = "CUSTOM_KEY";
//                    Object customVal = securityContext.get(customMapKey);
//                    if(Objects.nonNull(customVal) && customVal instanceof String strCustomVal) {
//                        return ;
//                    }
//                    log.warn("当前jwt未配置自定义key.[{}]", customMapKey);
//                }
//            });
            return new NimbusJwtDecoder(jwtProcessor);
        }

        @Bean
        public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
            return new NimbusJwtEncoder(jwkSource);
        }

    }
}
