package com.example.authserver.server.common.custom.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 长安
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientAuthenticationMethod {

    /*
        验证码
     */
    private boolean enableVerificationCode;
}
