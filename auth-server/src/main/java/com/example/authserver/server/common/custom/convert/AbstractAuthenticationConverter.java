package com.example.authserver.server.common.custom.convert;

import com.example.authserver.server.common.custom.AuthenticationMethodService;
import com.example.authserver.server.common.custom.UserAuthenticationFilter;
import com.example.authserver.server.common.custom.token.UserCustomAuthenticationToken;
import com.example.authserver.server.common.custom.extension.AuthenticationOperationExtension;
import com.example.authserver.server.common.custom.extension.DelegatingAuthenticationOperationExtension;
import com.example.authserver.server.common.custom.extension.VerificationCodeAuthenticationExtension;
import com.example.authserver.server.common.custom.service.DefaultAuthenticationMethodService;
import com.example.authserver.server.common.custom.user.ClientAuthenticationMethod;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.List;

/**
 * @author: 长安
 * parse request parameter [username, password ...]
 */
@Slf4j
public abstract class AbstractAuthenticationConverter implements AuthenticationConverter, Ordered {

    @Setter private String username = "username";
    @Setter private String password = "password";

    @Setter private AuthenticationOperationExtension<UserCustomAuthenticationToken> authenticationOperationExtension;
    @Setter @Getter private AuthenticationMethodService authenticationMethodService = new DefaultAuthenticationMethodService();

    public AbstractAuthenticationConverter() {
        authenticationOperationExtension = new DelegatingAuthenticationOperationExtension(
            List.of(
                new VerificationCodeAuthenticationExtension()
                // TODO 多因子验证其他扩展
            )
        );
    }


    @Override
    public Authentication convert(HttpServletRequest request) {

        String[] uap = findUsernameAndPassword(request);
        String username = StringUtils.isNotBlank(uap[0]) ? uap[0].trim() : null;
        String password = StringUtils.isNotBlank(uap[0]) ? uap[1].trim() : null;

        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new InternalAuthenticationServiceException("UserAuthenticationFilter -> username or password is null!");
        }

        ClientAuthenticationMethod clientAuthenticationMethod = authenticationMethodService.findUserAuthenticationMethod(findServiceId(request));
        UserCustomAuthenticationToken requestAuthentication = UserCustomAuthenticationToken
            .of(username, password)
            .authenticationMethodSwitch(clientAuthenticationMethod);

        try{
            authenticationOperationExtension.extraAuthentication(requestAuthentication, request);
            authenticationOperationExtension.preAuthenticationCheck(requestAuthentication, request);
        } catch (Throwable ex) {
            log.error("extra authentication or pre authentication check error", ex);
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
        return requestAuthentication;
    }

    /**
     * 获取认证客户端标识
     * @param request HttpServletRequest
     * @return client_id
     */
    private String findServiceId(HttpServletRequest request) {
        return request.getHeader(UserAuthenticationFilter.USER_AUTH_HEADER_KEY);
    }

    /**
     * 解析用户名、密码参数
     * @param request HttpServletRequest
     * @return new String[]{username, password}
     */
    protected String[] findUsernameAndPassword(HttpServletRequest request) {
        // TODO 抽象一个接口来处理各种类型请求的参数处理
        String username = request.getParameter(this.username);
        String password = request.getParameter(this.password);
        return new String[]{username, password};
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
