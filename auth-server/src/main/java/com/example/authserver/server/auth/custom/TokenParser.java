package com.example.authserver.server.auth.custom;

import org.springframework.security.core.Authentication;

/**
 * @author: 长安
 */
public interface TokenParser<T extends Authentication> {

    UserTokenAdapter encode(T t);

    T decode(String token);
}
