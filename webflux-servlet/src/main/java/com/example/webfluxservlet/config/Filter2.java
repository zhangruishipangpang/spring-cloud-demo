package com.example.webfluxservlet.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author: 长安
 *
 * 测试重定向 response 中信息。
 */
@Order(100)
@Component
public class Filter2 implements WebFilter {

    private transient Logger log = LoggerFactory.getLogger(Filter1.class);
    private final String REDIRECT_PATH = "re";
    private final String STORE_KEY = "M_KEY";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String value = exchange.getRequest().getPath().toString();
        log.info(" 请求接口: {} ", value);

        if(value.contains(REDIRECT_PATH)) {
            return Mono.fromRunnable(() -> {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.FOUND);
                URI newLocation = URI.create("/secondRe");
                response.getHeaders().setLocation(newLocation);
            })
                .contextWrite(context -> context.put(STORE_KEY, "测试"))
                .then(Mono.empty());
        }

        return Mono.defer(() -> chain.filter(exchange));
    }
}
