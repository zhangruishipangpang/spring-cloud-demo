package com.example.gateway.config.oauth2;

import com.example.gateway.config.oauth2.custom.BearerTokenParseToAuthenticationFilter;
import com.example.gateway.config.oauth2.custom.InMemoryTokenServerSecurityContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import reactor.core.publisher.Mono;

/**
 * @author: 长安
 */
@Slf4j
@Configuration
public class SecurityConfiguration {

    /*
        配置处理 Oauth2 client 的 Filter URL .
     */
    private String oauth2AuthenticationPath;

    public SecurityConfiguration() {
        this.oauth2AuthenticationPath = "/oauth2/authorization/okta";
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http
//            .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/**")) // 配置拦截链生效的请求path e.g. 默认全部
            .authorizeExchange(authorize  -> {
                authorize.pathMatchers("/unAuth/**", "/favicon.ico").permitAll();
                authorize.pathMatchers("/auth/**").hasAuthority("admin");
                authorize.anyExchange().authenticated();
        });

        http.csrf().disable()
            .formLogin().disable()
            .headers(headerSpec -> {
                headerSpec.frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.DENY); // 是否可在浏览器使用 iframe 等标签嵌入私有页面， 配置拒绝任何
            })
            .exceptionHandling(exceptionHandlingSpec -> {
                exceptionHandlingSpec.accessDeniedHandler((exchange, deniedException) -> {
                    log.info("access denied handler . \n exchange is [{}] , \n denied exception is [{}]", exchange, deniedException);
                    return Mono.defer(() -> exchange.getResponse().setComplete());
                });

//                exceptionHandlingSpec.authenticationEntryPoint((exchange, authenticationException) -> {
//                    log.info("authentication entry point . \n exchange is [{}] , \n ex is [{}]", exchange, authenticationException);
//                    return Mono.defer(() -> {
//                        exchange.getResponse().getHeaders().put("ERROR_MESSAGE", Arrays.asList("error authentication.", "custom error."));
//                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                        return exchange.getResponse().setComplete();
//                    });
//                });
                // 配置重定向 entry point
                exceptionHandlingSpec.authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint(this.oauth2AuthenticationPath));
            })
            .oauth2Client(oAuth2ClientSpec -> {
            })
            /*
                不同 Request 请求间的 SecurityContext 保留。
                e.g. 针对使用 JWT 这类认证 Session 的方式不需要配置，直接配置成无状态的 Repository
             */
//            .securityContextRepository(new InMemoryTokenServerSecurityContextRepository())
            .requestCache().disable() // 关闭请求重发，认证完成后直接返回默认的地址 / ，返回一个认证成功的信息，并加上 token
        ;
            /*
                添加一个自定义的 TOKEN 解析类
             */
            http.addFilterAt(new BearerTokenParseToAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

//    @Bean
//    public ReactiveClientRegistrationRepository reactiveClientRegistrationRepository() {
//
//        return new InMemoryReactiveClientRegistrationRepository();
//    }

}
