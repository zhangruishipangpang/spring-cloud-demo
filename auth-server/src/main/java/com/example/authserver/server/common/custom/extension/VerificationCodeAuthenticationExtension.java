package com.example.authserver.server.common.custom.extension;

import com.example.authserver.server.common.custom.CustomAuthenticationType;
import com.example.authserver.server.common.custom.token.UserCustomAuthenticationToken;
import com.example.authserver.server.common.custom.ex.VerificationCodeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import java.util.Objects;

/**
 * @author: 长安
 */
@Slf4j
public class VerificationCodeAuthenticationExtension implements AuthenticationOperationExtension<UserCustomAuthenticationToken> {

    public static final String VERIFICATION_CODE_PARAMETER_ID = "vId"; // verificationId
    public static final String VERIFICATION_CODE_PARAMETER_KEY = "vCode"; // verificationCode
    public static final Class<CustomAuthenticationType.verificationCodeType> VERIFICATION_CODE_CLASS = CustomAuthenticationType.verificationCodeType.class;


    @Override
    public void extraAuthentication(UserCustomAuthenticationToken authentication, HttpServletRequest request) {

        if(!authentication.enable(VERIFICATION_CODE_CLASS)) {
            return ;
        }

        String verificationCode = getVerificationCode(request);
        String verificationId = getVerificationId(request);

        if(log.isDebugEnabled()) {
            log.debug("[DEBUG]VerificationCodeAuthenticationExtension#extraAuthentication verificationCode -> [{}]", verificationCode);
        }
        authentication.with(VERIFICATION_CODE_CLASS, VERIFICATION_CODE_PARAMETER_KEY, verificationCode)
            .with(VERIFICATION_CODE_CLASS, VERIFICATION_CODE_PARAMETER_ID, verificationId);
        ;
    }

    @Override
    public void preAuthenticationCheck(UserCustomAuthenticationToken authentication, HttpServletRequest request) {

        if(!authentication.enable(VERIFICATION_CODE_CLASS)) {
            return ;
        }

        // 这里只校验验证码ID是否为空，值在认证时校验
        Object vId = authentication.from(VERIFICATION_CODE_CLASS, VERIFICATION_CODE_PARAMETER_ID);
        checkVerificationId(vId);

        AuthenticationOperationExtension.super.preAuthenticationCheck(authentication, request);
    }

    private void checkVerificationId(Object verificationCode) {
        // 检查验证码是否合规
        if(Objects.nonNull(verificationCode)
            && verificationCode instanceof String cvid) {

            return ;
        }
        throw new InternalAuthenticationServiceException("verification check error", new VerificationCodeException("verification check error, vid is null ..."));
    }

    private String getVerificationCode(HttpServletRequest request) {
        return request.getParameter(VERIFICATION_CODE_PARAMETER_KEY);
    }

    private String getVerificationId(HttpServletRequest request) {
        return request.getParameter(VERIFICATION_CODE_PARAMETER_ID);
    }
}
