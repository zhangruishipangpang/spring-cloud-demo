package com.example.authserver.server.common.custom;

import com.example.authserver.server.common.custom.ex.ClientAuthenticationMethodNotFoundException;
import com.example.authserver.server.common.custom.user.ClientAuthenticationMethod;

/**
 * @author: 长安
 */
public interface AuthenticationMethodService {

    ClientAuthenticationMethod findUserAuthenticationMethod(String clientName) throws ClientAuthenticationMethodNotFoundException;
}
