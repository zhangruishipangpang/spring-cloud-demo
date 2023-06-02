package com.example.authserver.server.common.custom.extension;

import com.example.authserver.server.common.custom.UserCustomAuthenticationToken;
import com.example.authserver.server.common.custom.ex.ExtensionException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * @author: 长安
 */
public class DelegatingAuthenticationOperationExtension implements AuthenticationOperationExtension<UserCustomAuthenticationToken> {

    private List<AuthenticationOperationExtension<UserCustomAuthenticationToken>> authenticationOperationExtensions;

    public DelegatingAuthenticationOperationExtension(List<AuthenticationOperationExtension<UserCustomAuthenticationToken>> authenticationOperationExtensions) {
        Objects.requireNonNull(authenticationOperationExtensions, "authenticationOperationExtensions is null");
        this.authenticationOperationExtensions = new LinkedList<>(authenticationOperationExtensions);
    }

    @Override
    public void extraAuthentication(UserCustomAuthenticationToken authentication, HttpServletRequest request) {
        for (AuthenticationOperationExtension<UserCustomAuthenticationToken> authenticationOperationExtension : authenticationOperationExtensions) {
            authenticationOperationExtension.extraAuthentication(authentication, request);
        }
    }

    @Override
    public void preAuthenticationCheck(UserCustomAuthenticationToken authentication, HttpServletRequest request) {
        for (AuthenticationOperationExtension<UserCustomAuthenticationToken> authenticationOperationExtension : authenticationOperationExtensions) {
            authenticationOperationExtension.preAuthenticationCheck(authentication, request);
        }
    }

    public void addAuthenticationOperationExtension(AuthenticationOperationExtension<UserCustomAuthenticationToken> authenticationOperationExtension) {
        this.authenticationOperationExtensions.add(Objects.requireNonNull(authenticationOperationExtension, "authenticationOperationExtension is null"));
    }

}
