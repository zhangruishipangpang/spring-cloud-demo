package com.example.authserver.server.auth;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Auther: 长安
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
@WebFilter(urlPatterns = "/**", filterName = "custom")
public class CustomFilter implements Filter {

    private transient Logger logger = LoggerFactory.getLogger(CustomFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        logger.info("current request inter path is [{}], FilterChain is [{}]", httpServletRequest.getServletPath(), chain);
        chain.doFilter(request, response);
        logger.info("current request outer path is [{}]", httpServletRequest.getServletPath());
    }
}
