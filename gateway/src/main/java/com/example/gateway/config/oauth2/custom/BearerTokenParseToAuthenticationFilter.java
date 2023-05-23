package com.example.gateway.config.oauth2.custom;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author: 长安
 * 解析 token 转成 Authentication 对象提供给 AuthorizationWebFilter
 */
public class BearerTokenParseToAuthenticationFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        Flux.fromIterable(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION))
//            .filter( token -> StringUtils.hasText( token ))
        return Mono.defer(() -> chain.filter(exchange).then(Mono.empty()));
    }


}
