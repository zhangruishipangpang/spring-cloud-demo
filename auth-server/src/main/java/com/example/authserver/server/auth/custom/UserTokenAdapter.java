package com.example.authserver.server.auth.custom;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author: 长安
 * 通用的返回适配器
 */
public class UserTokenAdapter extends LinkedHashMap<Class<?>, Object> {

    public UserTokenAdapter() {
        super(4);
    }

    public Object put(Class<?> key, Object value) {
        Objects.requireNonNull(key, "UserTokenAdapter#put key is null");
        return super.put(key, value);
    }

    public Object get(Class<?> key) {
        return super.get(key);
    }

    public static UserTokenAdapter of(Jwt jwt) {
        UserTokenAdapter userTokenAdapter = new UserTokenAdapter();
        userTokenAdapter.put(Jwt.class, jwt);
        return userTokenAdapter;
    }
}
