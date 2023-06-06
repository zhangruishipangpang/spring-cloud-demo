package com.example.authserver.server.auth.custom.token;

import com.example.authserver.server.auth.custom.JwtKeyProperties;
import com.example.authserver.server.auth.custom.TokenParser;
import com.example.authserver.server.auth.custom.UserTokenAdapter;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author: 长安
 */
@Slf4j
public class DefaultTokenParser implements TokenParser<Authentication> {

    JwtKeyProperties jwtKeyProperties = new JwtKeyProperties();

    final JwtEncoder jwtEncoder;
    final JwtDecoder jwtDecoder;
    final JwtConfigSettings settings;

    public DefaultTokenParser(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, JwtConfigSettings jwtConfigSettings) {
        Objects.requireNonNull(jwtEncoder, "jwtEncoder is null");
        Objects.requireNonNull(jwtDecoder, "jwtDecoder is null");
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.settings = jwtConfigSettings;
    }

    public DefaultTokenParser(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this(jwtEncoder, jwtDecoder, JwtConfigSettings.builder().build());
    }


    @Override
    public UserTokenAdapter encode(Authentication authentication) {
        Objects.requireNonNull(authentication, "DefaultTokenParser#encode authentication is null");
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).keyId(jwtKeyProperties.getKeyId()).build();
        JwtClaimsSet.Builder jwtClaimsSetBuilder = JwtClaimsSet.builder();
        jwtClaimsSetBuilder
            .id(Objects.requireNonNull(settings.jti.get()))
            .expiresAt(Objects.requireNonNull(settings.expiredAt.get()))
            .issuedAt(settings.issuedAt.get())
            .issuer(settings.iss.get())
            .subject(settings.sub.get())
            .claim(Authentication.class.getName(), authentication.getName())
        ;

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSetBuilder.build());
        Jwt jwt = jwtEncoder.encode(jwtEncoderParameters);
        return UserTokenAdapter.of(jwt);
    }

    @Override
    public Authentication decode(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            if(jwt.getClaims().get(Authentication.class.getName()) instanceof String username) {
                return new UsernamePasswordAuthenticationToken(username, null, null);
            }
            throw new InvalidBearerTokenException("claims username not String.class");
        } catch (Exception ex) {
            throw new InvalidBearerTokenException("Invalid Bearer Token. --c");
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JwtConfigSettings {
        @Builder.Default
        private Supplier<String> jti = () -> UUID.randomUUID().toString();
        @Builder.Default
        private Supplier<Instant> issuedAt = () -> Instant.now().plus(1, ChronoUnit.HOURS);
        @Builder.Default
        private Supplier<Instant> expiredAt = () -> Instant.now().plus(2, ChronoUnit.HOURS);
        @Builder.Default
        private Supplier<String> iss = () -> "http://127.0.0.1:9090/problem/";
        @Builder.Default
        private Supplier<String> sub = () -> "system"; // or third party
    }
}
