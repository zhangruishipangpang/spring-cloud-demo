package com.example.authserver.server.common.custom.ex;

/**
 * @author: 长安
 */
public class TokenException extends RuntimeException{

    public TokenException(String message) {
        super(message);
    }
}
