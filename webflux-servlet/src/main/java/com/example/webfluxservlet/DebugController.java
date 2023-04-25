package com.example.webfluxservlet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @Auther: 长安
 */
@RestController
public class DebugController {

    @GetMapping("/debug")
    public String debug() {
        return " debug ";
    }

    @GetMapping("/mono")
    public Mono<String> mono() {
        return Mono.just("single mono!");
    }

    @GetMapping("/sof")
    public String sof() {
        run();
        return "oom";
    }

    private void run() {
        Object o = new Object();
        run();
    }
}
