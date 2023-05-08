package com.example.authserver.view;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @Auther: 长安
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @PreAuthorize("hasAuthority('SCOPE_message.read')")
    @RequestMapping("/read")
    public Mono<String> resource1() {

        return Mono.just("message.read");
    }

    @PostAuthorize("hasAuthority('SCOPE_message.write')")
    @RequestMapping("/write")
    public Mono<String> resource2() {

        return Mono.just("message.write");
    }

}