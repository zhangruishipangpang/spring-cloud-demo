package com.example.webfluxservlet.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author: 长安
 *
 * 测试重定向 response 中信息。
 */
@Order(0)
@Component
public class Filter1 implements WebFilter {
    private transient Logger log = LoggerFactory.getLogger(Filter1.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        log.info("本次请求请求头：{}", exchange.getRequest().getHeaders());
        log.info("请求参数：{}", exchange.getRequest().getQueryParams());

        return Mono.defer(() -> chain.filter(exchange)).contextWrite(context -> context.put("KK", 1));
    }
}
