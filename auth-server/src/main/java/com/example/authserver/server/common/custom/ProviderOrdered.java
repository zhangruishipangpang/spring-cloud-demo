package com.example.authserver.server.common.custom;

import com.example.authserver.server.common.custom.provider.UserAuthenticationProvider;
import com.example.authserver.server.common.custom.provider.VerificationCodeProvider;
import lombok.Getter;

/**
 * @author: 长安
 */
public enum ProviderOrdered {

    // 基于用户名密码扩展的验证码验证
    VERIFICATION_CODE_PROVIDER,
    // 用户名密码基础认证
    USER_AUTHENTICATION_PROVIDER,
    ;
    private Class<? extends UserAuthenticationProvider> clz;

    private static final int INTERVAL = 100;
    @Getter private final int order;
    ProviderOrdered() {
        this.order = ordinal() * INTERVAL;
    }

}
