package com.example.authserver.server.common.custom;

import com.example.authserver.server.common.custom.extension.VerificationCodeAuthenticationExtension;
import com.example.authserver.server.common.custom.user.ClientAuthenticationMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 长安
 */
public class UserCustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Integer DEFAULT_INITIAL_CAPACITY = 1 << 2;
    public static final Object NULL_OBJ = new Object();

    private final Map<Class<? extends CustomAuthenticationType>, Boolean> authenticationTypeSwitch = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
    private final Map<Class<? extends CustomAuthenticationType>, Object> authenticationType = new HashMap<>(DEFAULT_INITIAL_CAPACITY);

    private UserCustomAuthenticationToken(String verificationCode, Object principal, Object credentials) {
        this(principal, credentials);
    }

    private UserCustomAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials); // 未认证
    }

    public UserCustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities); // 已认证
    }

    public static UserCustomAuthenticationToken of(Object principal, Object credentials) {
        return new UserCustomAuthenticationToken(principal, credentials);
    }

    public static UserCustomAuthenticationToken of(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new UserCustomAuthenticationToken(principal, credentials, authorities);
    }

    public UserCustomAuthenticationToken with(Class<? extends CustomAuthenticationType> authenticationType, Object materials) {
        this.putAuthenticationType(authenticationType, materials);
        return this;
    }

    public UserCustomAuthenticationToken authenticationMethodSwitch(ClientAuthenticationMethod clientAuthenticationMethod) {
        putAuthenticationTypeSwitch(VerificationCodeAuthenticationExtension.VERIFICATION_CODE_CLASS, clientAuthenticationMethod.isEnableVerificationCode());
        return this;
    }

    public Boolean enable(Class<? extends CustomAuthenticationType> authenticationType) {
        return getAuthenticationTypeSwitch(authenticationType);
    }

    public Object from(Class<? extends CustomAuthenticationType> authenticationType) {
        return this.getAuthenticationType(authenticationType);
    }

    private void putAuthenticationType(Class<? extends CustomAuthenticationType> authenticationType, Object materials) {
        this.authenticationType.put(authenticationType, materials);
    }

    private Object getAuthenticationType(Class<? extends CustomAuthenticationType> authenticationType) {
        return this.authenticationType.getOrDefault(authenticationType, NULL_OBJ);
    }

    private void putAuthenticationTypeSwitch(Class<? extends CustomAuthenticationType> authenticationType, Boolean enable) {
        this.authenticationTypeSwitch.put(authenticationType, enable);
    }

    private Boolean getAuthenticationTypeSwitch(Class<? extends CustomAuthenticationType> authenticationType) {
        return this.authenticationTypeSwitch.getOrDefault(authenticationType, Boolean.FALSE);
    }
}
