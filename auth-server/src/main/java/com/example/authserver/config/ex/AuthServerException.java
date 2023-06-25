package com.example.authserver.config.ex;

/**
 * @author: 长安
 * 认证服务抛出的异常信息
 */
public class AuthServerException extends RuntimeException {

    public AuthServerException(String message) {
        super(message);
    }
}
