package com.example.authserver.server.common.custom.service;

import com.example.authserver.server.common.custom.AuthenticationMethodService;
import com.example.authserver.server.common.custom.ex.ClientAuthenticationMethodNotFoundException;
import com.example.authserver.server.common.custom.user.ClientAuthenticationMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: 长安
 * 客户端验证方式 - 验证码开启
 */
@Slf4j
public class DefaultAuthenticationMethodService implements AuthenticationMethodService {

    @Override
    public ClientAuthenticationMethod findUserAuthenticationMethod(String clientName) throws ClientAuthenticationMethodNotFoundException {
        if(log.isDebugEnabled()) {
            log.debug("[DEBUG]clientName[{}]", clientName);
        }
        return ClientAuthenticationMethod.builder()
            .enableVerificationCode(true)
            .build();
    }
}
