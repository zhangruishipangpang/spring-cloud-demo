package com.example.authserver.config;

import com.example.authserver.server.common.custom.store.VerificationCodeStoreService;
import com.example.authserver.server.common.custom.store.VerificationCodeStoreServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.util.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.List;
import java.util.Properties;

/**
 * @author: 长安
 */
@EnableDiscoveryClient(autoRegister = false)
@Configuration
public class CommonConfiguration {

    @Bean
    public Config kaptchaConfig() {
        // 配置图形验证码的基本参数
        Properties properties = new Properties();
        // 图片宽度
        properties.setProperty("kaptcha.image.width", "150");
        // 图片长度
        properties.setProperty("kaptcha.image.height", "50");
        // 字符集
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        // 字符长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        return new Config(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public VerificationCodeStoreService verificationCodeStoreService() {
        return new VerificationCodeStoreServiceImpl();
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new CoreJackson2Module());
        // ... your other configuration
//        return mapper;
//    }
}
