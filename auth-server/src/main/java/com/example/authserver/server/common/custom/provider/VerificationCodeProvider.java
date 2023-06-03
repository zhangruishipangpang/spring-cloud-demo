package com.example.authserver.server.common.custom.provider;

import com.example.authserver.server.common.custom.ProviderOrdered;
import com.example.authserver.server.common.custom.UserCustomAuthenticationToken;
import com.example.authserver.server.common.custom.extension.VerificationCodeAuthenticationExtension;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author: 长安
 */
@Slf4j
@Service
public class VerificationCodeProvider extends UserAuthenticationProvider {

    public VerificationCodeProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        super(userDetailsService, passwordEncoder);
    }

    @Override
    protected void preUserPasswordMatchChecks(UserCustomAuthenticationToken authentication) {

        if(authentication.enable(VerificationCodeAuthenticationExtension.VERIFICATION_CODE_CLASS)) {
            try {
                if(authentication.from(VerificationCodeAuthenticationExtension.VERIFICATION_CODE_CLASS) instanceof String verificationCode) {
                    if(log.isDebugEnabled()) {
                        log.debug("[DEBUG]VerificationCodeProvider#preUserPasswordMatchChecks verification code -> [{}]", verificationCode);
                    }
                    checkValid(verificationCode);
                } else {
                    throw new InternalAuthenticationServiceException("VerificationCodeProvider#preUserPasswordMatchChecks verificationCode not String");
                }
            } catch (ClassCastException ex) {
                log.error("VerificationCodeProvider#preUserPasswordMatchChecks verificationCode class cast error. ", ex);
                throw new InternalAuthenticationServiceException("verificationCode is null");
            } catch (Exception ex) {
                log.error("VerificationCodeProvider#preUserPasswordMatchChecks verificationCode check error. ", ex);
                throw  new InternalAuthenticationServiceException("verificationCode check error");
            }
        }
        super.preUserPasswordMatchChecks(authentication);
    }

    private void checkValid(String verificationCode) throws Exception {
        // for testing
        if("error".equals(verificationCode)) {
            throw new Exception("验证失败！");
        }
    }

    @Override
    public int getOrder() {
        return ProviderOrdered.VERIFICATION_CODE_PROVIDER.getOrder();
    }
}
