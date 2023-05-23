package com.example.gateway.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author: 长安
 */
@Slf4j
@RestController
@RequestMapping("/unAuth")
public class CodeReceiveController {

    @GetMapping("/receiver")
    public Mono<String> receiver(@RequestParam("code") String code) {
        log.info("receive authorization_code is [{}]", code);
        return Mono.just(code);
    }
}
