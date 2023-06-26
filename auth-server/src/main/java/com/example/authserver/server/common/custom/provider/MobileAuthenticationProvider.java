package com.example.authserver.server.common.custom.provider;

import com.example.authserver.server.common.custom.ProviderOrdered;
import com.example.authserver.server.common.custom.store.SmsCodeStoreService;
import com.example.authserver.server.common.custom.token.MobileAuthenticationToken;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * @author: 长安
 */
@Component
public class MobileAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements Ordered {

    @Getter private final UserDetailsService userDetailsService;
    @Getter private final PasswordEncoder passwordEncoder;
    private final SmsCodeStoreService smsCodeStoreService;

    public MobileAuthenticationProvider(@Qualifier("mobileApplicationUserDetailsService") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, SmsCodeStoreService smsCodeStoreService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.smsCodeStoreService = smsCodeStoreService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken) authentication;
        if (mobileAuthenticationToken.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

        String mobile = mobileAuthenticationToken.getName();
        String smsCode = mobileAuthenticationToken.getCredentials().toString();
        if(!smsCodeStoreService.hasSmsCode(mobile)) {
            this.logger.debug("短信息验证码未发送或者已过期！");
            throw new BadCredentialsException("短信息验证码未发送或者已过期");
        }
        String storeSmsCode = smsCodeStoreService.getSmsCode(mobile);
        if (StringUtils.isBlank(storeSmsCode) || !storeSmsCode.equals(smsCode)) {
            this.logger.debug(format("短信息验证码验证失败！请求验证码:[%s], store验证码:[%s]", smsCode, storeSmsCode));
            throw new BadCredentialsException("短信息验证码验证失败");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        try {
            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                    "MobileUserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        }
        catch (UsernameNotFoundException ex) {
            throw ex;
        }
        catch (InternalAuthenticationServiceException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public int getOrder() {
        return ProviderOrdered.MOBILE_AUTHENTICATION_PROVIDER.getOrder();
    }
}
