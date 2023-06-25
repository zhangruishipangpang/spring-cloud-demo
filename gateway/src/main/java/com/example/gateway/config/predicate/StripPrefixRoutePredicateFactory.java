package com.example.gateway.config.predicate;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Predicate;

/**
 * @author: 长安
 * Strip Prefix Custom
 */
@Component
public class StripPrefixRoutePredicateFactory extends AbstractRoutePredicateFactory<Object>  {


    public StripPrefixRoutePredicateFactory() {
        super(Object.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Object config) {

        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange exchange) {

                String path = exchange.getRequest().getURI().getRawPath();
                String[] split = path.split("/");
                String newPath = "";
                if(split.length <= 1) {
                    newPath = "/";
                } else {
                    for (int i = 2; i < split.length; i++) {
                        newPath = newPath + "/" + split[i];
                    }
                }
                ServerHttpRequest newRequest = exchange.getRequest().mutate().path(newPath).build();
                exchange.mutate().request(newRequest).build();
                return true;
            }
        };
    }
}
