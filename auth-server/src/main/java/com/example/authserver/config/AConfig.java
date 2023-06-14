package com.example.authserver.config;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

/**
 * @author: 长安
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class AConfig implements EnvironmentAware {

    @Override
    public void setEnvironment(Environment environment) {
        String url = environment.getProperty("spring.datasource.url");
        String user = environment.getProperty("spring.datasource.username");
        String pass = environment.getProperty("spring.datasource.driver-class-name");

        log.info("==========> {} - {} - {}", url, user, pass);
    }
}
