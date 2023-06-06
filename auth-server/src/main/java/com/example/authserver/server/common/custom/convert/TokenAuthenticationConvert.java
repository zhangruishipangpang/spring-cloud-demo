package com.example.authserver.server.common.custom.convert;

import com.example.authserver.server.auth.custom.TokenParser;
import com.example.authserver.server.common.custom.store.TokenStoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

/**
 * @author: 长安
 */
@SuppressWarnings("rawtypes")
public class TokenAuthenticationConvert implements AuthenticationConverter {

    private final TokenParser tokenParser;
    private final TokenStoreService<Authentication, String> tokenStoreService;

    public TokenAuthenticationConvert(TokenParser tokenParser, TokenStoreService<Authentication, String> tokenStoreService) {
        this.tokenParser = tokenParser;
        this.tokenStoreService = tokenStoreService;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        String token = findHeaderToken(request);
        Authentication authentication = tokenParser.decode(token);
        return tokenStoreService.getTokenAuthentication(token);
    }

    private String findHeaderToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.isBlank(token) || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring("Bearer ".length());
    }
}
