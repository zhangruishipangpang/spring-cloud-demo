package com.example.authserver.server.common.custom.extension;

import com.example.authserver.server.common.custom.CustomAuthenticationType;
import com.example.authserver.server.common.custom.UserCustomAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

/**
 * @author: 长安
 */
public class VerificationCodeAuthenticationExtension implements AuthenticationOperationExtension<UserCustomAuthenticationToken> {

    public static final String VERIFICATION_CODE_PARAMETER_KEY = "verificationCode";
    public static final Class<CustomAuthenticationType.verificationCodeType> VERIFICATION_CODE_CLASS = CustomAuthenticationType.verificationCodeType.class;


    @Override
    public void extraAuthentication(UserCustomAuthenticationToken authentication, HttpServletRequest request) {

        if(!authentication.enable(VERIFICATION_CODE_CLASS)) {
            return ;
        }

        String verificationCode = getVerificationCode(request);
        authentication.with(VERIFICATION_CODE_CLASS, verificationCode);
    }

    @Override
    public void preAuthenticationCheck(UserCustomAuthenticationToken authentication, HttpServletRequest request) {

        if(!authentication.enable(VERIFICATION_CODE_CLASS)) {
            return ;
        }

        Object code = authentication.from(VERIFICATION_CODE_CLASS);
        if(Objects.nonNull(code) && code instanceof String vc) {
            check(vc);
        }
        AuthenticationOperationExtension.super.preAuthenticationCheck(authentication, request);
    }

    private void check(String verificationCode) {
        // 检查验证码是否合规
    }

    private String getVerificationCode(HttpServletRequest request) {
        return request.getParameter(VERIFICATION_CODE_PARAMETER_KEY);
    }
}
