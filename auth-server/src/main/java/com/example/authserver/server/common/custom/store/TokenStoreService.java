package com.example.authserver.server.common.custom.store;

import org.springframework.security.core.Authentication;

/**
 * @author: 长安
 */
public interface TokenStoreService {

    /**
     *
     * @param authentication 认证用户信息
     * @param token token
     * @return username
     */
    String setToken(Authentication authentication, String token);

    /**
     *
     * @param token token
     * @return true / false
     */
    boolean hasToken(String token);

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
    Authentication getTokenAuthentication(String token);

    /**
     *
     * @param username username
     * @return 认证用户信息
     */
    Authentication getUserAuthentication(String username);

    /**
     *
     * @param username username
     * @return 认证用户信息
     */
    Authentication evictToken(String username);

    /**
     *
     * @param token token
     * @return 认证用户信息
     */
    Authentication evictUser(String token);

}
