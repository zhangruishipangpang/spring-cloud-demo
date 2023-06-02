package com.example.authserver.server.common.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * @author: 长安
 * custom authentication provider
 * for extension verificationCode and so on.
 * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}
 */
public class UserAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    /**
     * The plaintext password used to perform
     * {@link PasswordEncoder#matches(CharSequence, String)} on when the user is not found
     * to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    /**
     * The password used to perform {@link PasswordEncoder#matches(CharSequence, String)}
     * on when the user is not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private volatile String userNotFoundEncodedPassword;


    @Getter final private UserDetailsService userDetailsService;
    @Getter final private PasswordEncoder passwordEncoder;

    public UserAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        if(Objects.isNull(passwordEncoder)) {
            this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        } else {
            this.passwordEncoder = passwordEncoder;
        }
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserCustomAuthenticationToken customAuthenticationToken = (UserCustomAuthenticationToken) authentication;
        if (customAuthenticationToken.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        String presentedPassword = customAuthenticationToken.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(this.messages
                .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    @Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        prepareTimingAttackProtection();
        try {

            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        }
        catch (UsernameNotFoundException ex) {
            mitigateAgainstTimingAttack(authentication);
            throw ex;
        }
        catch (InternalAuthenticationServiceException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        Objects.requireNonNull(this.userDetailsService, "userDetailsService is null");
    }

    /**
     * 处理密码之前的验证
     */
    protected void preUserPasswordMatchChecks(UsernamePasswordAuthenticationToken authentication) {
        
    }
}
