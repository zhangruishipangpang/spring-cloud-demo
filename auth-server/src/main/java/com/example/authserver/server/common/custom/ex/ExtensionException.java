package com.example.authserver.server.common.custom.ex;

/**
 * @author: 长安
 * 扩展认证方式处理异常
 */
public class ExtensionException extends RuntimeException{

    public ExtensionException(String message) {
        super(message);
    }

    public ExtensionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
