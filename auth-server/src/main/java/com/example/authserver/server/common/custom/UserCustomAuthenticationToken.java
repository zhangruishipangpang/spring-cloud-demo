package com.example.authserver.server.common.custom;

import com.example.authserver.server.common.custom.extension.VerificationCodeAuthenticationExtension;
import com.example.authserver.server.common.custom.user.ClientAuthenticationMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: 长安
 */
public class UserCustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Integer DEFAULT_INITIAL_CAPACITY = 1 << 2;
    public static final Object NULL_OBJ = new Object();

    private final Map<Class<? extends CustomAuthenticationType>, Boolean> authenticationTypeSwitch = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
    private final Map<Class<? extends CustomAuthenticationType>, Map<String, Object>> authenticationType = new HashMap<>(DEFAULT_INITIAL_CAPACITY);

    private UserCustomAuthenticationToken(String verificationCode, Object principal, Object credentials) {
        this(principal, credentials);
    }

    private UserCustomAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials); // 未认证
    }

    public UserCustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities); // 已认证
    }

    public static UserCustomAuthenticationToken of(Authentication authentication) {
        Objects.requireNonNull(authentication, "usernamePasswordAuthenticationToken is null");
        UserCustomAuthenticationToken userCustomAuthenticationToken = new UserCustomAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authentication.getAuthorities());
        userCustomAuthenticationToken.setDetails(userCustomAuthenticationToken.getDetails());
        return userCustomAuthenticationToken;
    }

    public static UserCustomAuthenticationToken of(Object principal, Object credentials) {
        return new UserCustomAuthenticationToken(principal, credentials);
    }

    public static UserCustomAuthenticationToken of(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new UserCustomAuthenticationToken(principal, credentials, authorities);
    }

    public UserCustomAuthenticationToken with(Class<? extends CustomAuthenticationType> authenticationType, String materialKey, Object materials) {
        this.putAuthenticationType(authenticationType, materialKey, materials);
        return this;
    }

    public UserCustomAuthenticationToken authenticationMethodSwitch(ClientAuthenticationMethod clientAuthenticationMethod) {
        putAuthenticationTypeSwitch(VerificationCodeAuthenticationExtension.VERIFICATION_CODE_CLASS, clientAuthenticationMethod.isEnableVerificationCode());
        return this;
    }

    public Boolean enable(Class<? extends CustomAuthenticationType> authenticationType) {
        return getAuthenticationTypeSwitch(authenticationType);
    }

    public Object from(Class<? extends CustomAuthenticationType> authenticationType, String materialKey) {
        return this.getAuthenticationType(authenticationType, materialKey);
    }

    public String fromStrThrows(Class<? extends CustomAuthenticationType> authenticationType, String materialKey) {
        if(this.getAuthenticationType(authenticationType, materialKey) instanceof String var1) {
            return var1;
        }
        throw new ClassCastException("UserCustomAuthenticationToken#fromStr return value not String.class");
    }

    public String fromStr(Class<? extends CustomAuthenticationType> authenticationType, String materialKey) {
        if(this.getAuthenticationType(authenticationType, materialKey) instanceof String var1) {
            return var1;
        }
        return null;
    }

    private void putAuthenticationType(Class<? extends CustomAuthenticationType> authenticationType, String materialKey, Object materials) {
        this.authenticationType.compute(authenticationType, (key, old) -> {
            Map<String, Object> store = old;
            if(Objects.isNull(store)) {
                store = new HashMap<>(4);
            }
            store.put(materialKey, materials);
            return store;
        });
    }

    private Object getAuthenticationType(Class<? extends CustomAuthenticationType> authenticationType, String materialKey) {
        Map<String, Object> store = this.authenticationType.get(authenticationType);
        if(CollectionUtils.isEmpty(store)) {
            return NULL_OBJ;
        }
        return store.getOrDefault(materialKey, NULL_OBJ);
    }

    private void putAuthenticationTypeSwitch(Class<? extends CustomAuthenticationType> authenticationType, Boolean enable) {
        this.authenticationTypeSwitch.put(authenticationType, enable);
    }

    private Boolean getAuthenticationTypeSwitch(Class<? extends CustomAuthenticationType> authenticationType) {
        return this.authenticationTypeSwitch.getOrDefault(authenticationType, Boolean.FALSE);
    }
}
