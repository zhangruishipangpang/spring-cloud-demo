package com.example.authserver.server.common.custom.store;

import org.springframework.security.core.Authentication;

/**
 * @author: 长安
 */
public interface TokenStoreService<A, T> {

    /**
     *
     * @param authentication 认证用户信息
     * @param token token
     * @return username
     */
    String setToken(A authentication, T token);

    /**
     *
     * @param token token
     * @return true / false
     */
    boolean hasToken(T token);

    /**
     *
     * @param username username
     * @return true / false
     */
    boolean hasUser(String username);

    /**
     *
     * @param token token
     * @return 认证用户信息
     */
    A getTokenAuthentication(T token);

    /**
     *
     * @param username username
     * @return 认证用户信息
     */
    A getUserAuthentication(String username);

    /**
     *
     * @param username username
     * @return 认证用户信息
     */
    A evictToken(String username);

    /**
     *
     * @param token token
     * @return 认证用户信息
     */
    A evictUser(String token);

}
