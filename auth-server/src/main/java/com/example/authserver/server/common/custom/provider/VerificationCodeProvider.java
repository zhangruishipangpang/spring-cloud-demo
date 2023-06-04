package com.example.authserver.server.common.custom.provider;

import com.example.authserver.server.common.custom.ProviderOrdered;
import com.example.authserver.server.common.custom.UserCustomAuthenticationToken;
import com.example.authserver.server.common.custom.ex.VerificationCodeException;
import com.example.authserver.server.common.custom.extension.VerificationCodeAuthenticationExtension;
import com.example.authserver.server.common.custom.store.VerificationCodeStoreService;
import com.example.authserver.server.common.custom.store.VerificationCodeStoreServiceImpl;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @Setter private VerificationCodeStoreService verificationCodeStoreService;

    public VerificationCodeProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, VerificationCodeStoreService verificationCodeStoreService) {
        super(userDetailsService, passwordEncoder);
        this.verificationCodeStoreService = verificationCodeStoreService;
    }

    @Override
    protected void preUserPasswordMatchChecks(UserCustomAuthenticationToken authentication) {

        if(authentication.enable(VerificationCodeAuthenticationExtension.VERIFICATION_CODE_CLASS)) {
            try {
                String verificationCode = authentication.fromStrThrows(VerificationCodeAuthenticationExtension.VERIFICATION_CODE_CLASS, VerificationCodeAuthenticationExtension.VERIFICATION_CODE_PARAMETER_KEY);
                String verificationId = authentication.fromStrThrows(VerificationCodeAuthenticationExtension.VERIFICATION_CODE_CLASS, VerificationCodeAuthenticationExtension.VERIFICATION_CODE_PARAMETER_ID);
                if(log.isDebugEnabled()) {
                    log.debug("[DEBUG]VerificationCodeProvider#preUserPasswordMatchChecks verification code -> [{}]", verificationCode);
                }
                checkValid(verificationId, verificationCode);
            } catch (ClassCastException ex) {
                log.error("VerificationCodeProvider#preUserPasswordMatchChecks verificationCode or verificationId classCast error. ", ex);
                throw new InternalAuthenticationServiceException("verificationCode is null", ex);
            } catch (Exception ex) {
                log.error("VerificationCodeProvider#preUserPasswordMatchChecks verificationCode check error. ", ex);
                throw new InternalAuthenticationServiceException("verificationCode check error", ex);
            }
        }
        super.preUserPasswordMatchChecks(authentication);
    }

    private void checkValid(String verificationId, String verificationCode) throws Exception {
        // for testing
        if(!verificationCodeStoreService.hasVerificationCode(verificationId) || "error".equals(verificationCode)) {
            throw new Exception("验证失败！");
        }

        String storeVCode = verificationCodeStoreService.getVerificationCode(verificationId);
        if(StringUtils.isBlank(storeVCode) || !storeVCode.equals(verificationCode)) {
            throw new VerificationCodeException("verificationCode not match");
        }
        verificationCodeStoreService.evictVerificationCode(verificationId);
    }

    @Override
    public int getOrder() {
        return ProviderOrdered.VERIFICATION_CODE_PROVIDER.getOrder();
    }
}
