package com.example.authserver.server.common.custom.ex;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * @author: 长安
 * 验证码异常
 */
public class VerificationCodeException extends RuntimeException {

    public VerificationCodeException(String message) {
        super(message);
    }
}
