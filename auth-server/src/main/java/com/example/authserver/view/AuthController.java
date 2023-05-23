package com.example.authserver.view;

import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @Auther: 长安
 */
@RestController
public class AuthController {

    @GetMapping("/auth/token")
    public Mono<String> authToken() {

        return Mono.just("authToken");
    }

    @GetMapping("/auth")
    public Mono<String> auth() {

        return Mono.just("auth");
    }

    @PostMapping("/auth")
    public Mono<String> auth2() {

        return Mono.just("auth2");
    }

    @GetMapping("/unAuth")
    public Mono<String> unAuth() {

        return Mono.just("unAuth");
    }

    @PostMapping("/unAuth")
    public Mono<String> unAuth2() {

        return Mono.just("unAuth2");
    }

    @GetMapping("/admin")
    public Mono<String> admin() {

        return Mono.just("admin");
    }

    @GetMapping("/guest")
    public Mono<String> guest() {

        return Mono.just("guest");
    }


//    @PreAuthorize("hasRole('role1')")
    @GetMapping("/role")
    public Mono<String> role() {

        return Mono.just("role");
    }

    @GetMapping("/")
    public Mono<String> loginS() {

        return Mono.just("login success!");
    }

    @GetMapping("/authed")
    public Mono<String> authed() {

        return Mono.just("authed");
    }
}
