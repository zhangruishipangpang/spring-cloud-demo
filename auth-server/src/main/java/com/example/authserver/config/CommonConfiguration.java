package com.example.authserver.config;

import org.apache.catalina.core.ApplicationFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;

/**
 * @author: 长安
 */
@Configuration
public class CommonConfiguration {

    private final Map<String, SecurityFilterChain> stringSecurityFilterChainMap;
    private final Map<String, FilterChainProxy> filterChainProxyMap;
    private final Map<String, ApplicationFilterChain> applicationFilterChainMap;

    public CommonConfiguration(Map<String, SecurityFilterChain> stringSecurityFilterChainMap, Map<String, FilterChainProxy> filterChainProxyMap, Map<String, ApplicationFilterChain> applicationFilterChainMap) {
        this.stringSecurityFilterChainMap = stringSecurityFilterChainMap;
        this.filterChainProxyMap = filterChainProxyMap;
        this.applicationFilterChainMap = applicationFilterChainMap;
    }
}
