package com.example.authserver.server.common.custom.extension;

import com.example.authserver.server.common.custom.UserCustomAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import java.util.Objects;

/**
 * @author: 长安
 * 扩展验证方式，通常用与在用户名密码之外扩展验证码等
 */
public interface AuthenticationOperationExtension<A extends UserCustomAuthenticationToken> {

    /**
     * 将扩展信息放入Authentication中，用于后续认证获取
     * @param authentication 待认证 token
     * @param request HttpServletRequest
     */
    void extraAuthentication(A authentication, HttpServletRequest request);

    /**
     * 检查项
     * @param authentication 待认证 token
     * @param request HttpServletRequest
     */
    default void preAuthenticationCheck(A authentication, HttpServletRequest request) {
        Objects.requireNonNull(authentication.getPrincipal(), "username is null");
        Objects.requireNonNull(authentication.getCredentials(), "password is null");
    }

    @Slf4j
    public class NullAuthenticationOperationExtension implements AuthenticationOperationExtension<UserCustomAuthenticationToken> {

        @Override
        public void extraAuthentication(UserCustomAuthenticationToken authentication, HttpServletRequest request) {
            log.info("NullAuthenticationOperationExtension#extraAuthentication trigger do nothing");
        }
    }
}
