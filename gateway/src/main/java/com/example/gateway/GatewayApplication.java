package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        Hooks.onOperatorDebug(); // 开启 reactor 全局堆栈追踪

        SpringApplication.run(GatewayApplication.class, args);
    }

}
