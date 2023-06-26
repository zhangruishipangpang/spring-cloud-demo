package com.example.authserver.server.common.custom.convert;

import com.example.authserver.server.common.custom.token.MobileAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;

/**
 * @author: 长安
 */
public class MobileAuthenticationConverter extends AbstractAuthenticationConverter {

    @Setter private String mobileKey = "mobile";
    @Setter private String smsCodeKey = "smsCode";


    @Override
    public Authentication convert(HttpServletRequest request) {

        String mobile = findMobileValue(request);
        String smsCode = findSmsCodeValue(request);

        if(StringUtils.isBlank(mobile)) {
            return null;
        }

        return new MobileAuthenticationToken(mobile, smsCode);
    }

    private String findMobileValue(HttpServletRequest request) {
        return request.getParameter(mobileKey);
    }

    private String findSmsCodeValue(HttpServletRequest request) {
        return request.getParameter(smsCodeKey);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10 * 10000;
    }
}
