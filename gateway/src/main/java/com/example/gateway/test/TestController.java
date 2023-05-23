package com.example.gateway.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author: 长安
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("test");
    }
}
