package com.example.gateway.config.oauth2.custom;

import org.apache.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: 长安
 *
 */
public class InMemoryTokenServerSecurityContextRepository implements ServerSecurityContextRepository {

    /*
        存储用户认证的仓库
     */
    private final ConcurrentMap<String , SecurityContext> tokenStore = new ConcurrentHashMap<>();

    private AuthenticationManager authenticationManager;
    private final SecurityContext NULL = null;

    public InMemoryTokenServerSecurityContextRepository() {
        // TODO init authenticationManager
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        // 无状态，不保存
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {

        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        SecurityContext context = Optional.ofNullable(token)
            .map(f -> tokenStore.getOrDefault(f, NULL))
            .filter(securityContext -> !securityContext.equals(NULL))
            .orElseGet(() -> {
                Authentication authenticate = authenticationManager.authenticate(new BearerTokenAuthenticationToken(token));
                SecurityContext securityContext = new SecurityContextImpl(authenticate);
                tokenStore.put(token, securityContext);
                return securityContext;
            });
        return Mono.just(context);
    }
}
