package com.example.authserver.server.auth.custom.token;

import com.example.authserver.server.auth.custom.JwtKeyProperties;
import com.example.authserver.server.auth.custom.TokenParser;
import com.example.authserver.server.auth.custom.UserTokenAdapter;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
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

/**
 * @author: 长安
 */
@Slf4j
public class DefaultTokenParser implements TokenParser<Authentication> {

    JwtKeyProperties jwtKeyProperties = new JwtKeyProperties();

    final JwtEncoder jwtEncoder;
    final JwtDecoder jwtDecoder;

    public DefaultTokenParser(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }


    @Override
    public UserTokenAdapter encode(Authentication authentication) {
        Objects.requireNonNull(authentication, "DefaultTokenParser#encode authentication is null");
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).keyId(jwtKeyProperties.getKeyId()).build();
        JwtClaimsSet.Builder jwtClaimsSetBuilder = JwtClaimsSet.builder();
        jwtClaimsSetBuilder.id(UUID.randomUUID().toString())
            .issuedAt(Instant.now().plus(1, ChronoUnit.HOURS))
            .expiresAt(Instant.now().plus(3, ChronoUnit.HOURS))
            .claim(Authentication.class.getName(), authentication.getPrincipal())
            .issuer("http://127.0.0.1:9090/problem/issuer")
            .subject("custom subject")
        ;

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSetBuilder.build());
        Jwt jwt = jwtEncoder.encode(jwtEncoderParameters);
        return UserTokenAdapter.of(jwt);
    }

    @Override
    public Authentication decode(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        Object o = jwt.getClaims().get(Authentication.class.getName());
        LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) o;
        try {
            return new UsernamePasswordAuthenticationToken(map.get("username"), null, null);
        } catch (Exception ex) {
            throw new InvalidBearerTokenException("Invalid Bearer Token. --c");
        }
    }
}
