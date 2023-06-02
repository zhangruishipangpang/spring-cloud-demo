package com.example.authserver.server.common.custom.ex;

/**
 * @author: 长安
 * 客户端开启的认证方式 - 多因子
 */
public class ClientAuthenticationMethodNotFoundException extends RuntimeException{

    public ClientAuthenticationMethodNotFoundException(String message) {
        super(message);
    }
}
