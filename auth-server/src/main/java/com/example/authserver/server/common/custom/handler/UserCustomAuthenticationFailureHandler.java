package com.example.authserver.server.common.custom.handler;

import com.example.authserver.util.HttpMessageConverters;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 长安
 */
public class UserCustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final HttpMessageConverter<AuthenticationErrorResponse> errorResponseHttpMessageConverter = new AuthenticationErrorResponseHttpMessageConverter();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        AuthenticationErrorResponse.AuthenticationErrorResponseBuilder errorResponse = AuthenticationErrorResponse
            .builder().msg(exception.getMessage()).code("10001");

        errorResponseHttpMessageConverter.write(errorResponse.build(), null, new ServletServerHttpResponse(response));
    }


    public static class AuthenticationErrorResponseHttpMessageConverter extends AbstractHttpMessageConverter<AuthenticationErrorResponse> {

        private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

        private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        GenericHttpMessageConverter<Object> jsonHttpMessageConverter = HttpMessageConverters.getJsonMessageConverter();

        Converter<AuthenticationErrorResponse, Map<String, Object>> authenticationErrorResponseMapConverter = new AuthenticationErrorConvert();

        @Override
        protected boolean supports(@NotNull Class<?> clazz) {
            return AuthenticationErrorResponse.class.isAssignableFrom(clazz);
        }

        @Override
        protected AuthenticationErrorResponse readInternal(Class<? extends AuthenticationErrorResponse> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
            throw new UnsupportedOperationException("unsupported read");
        }

        @Override
        protected void writeInternal(@NotNull AuthenticationErrorResponse authenticationErrorResponse, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
            Map<String, Object> response = authenticationErrorResponseMapConverter.convert(authenticationErrorResponse);
            jsonHttpMessageConverter.write(response, STRING_OBJECT_MAP.getType(), MediaType.APPLICATION_JSON, outputMessage);

        }
    }


    public static class AuthenticationErrorConvert implements Converter<AuthenticationErrorResponse, Map<String, Object>> {

        @Override
        public Map<String, Object> convert(AuthenticationErrorResponse source) {
            return Map.of(
                "code", source.getCode(),
                "msg", source.getMsg()
            );
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthenticationErrorResponse {
        private String code;
        private String msg;
        private Map<String, Object> additionalParameter;
    }

}
