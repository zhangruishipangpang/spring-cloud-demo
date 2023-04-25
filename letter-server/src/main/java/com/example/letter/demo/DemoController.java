package com.example.letter.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: 长安
 */
@RestController
public class DemoController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
