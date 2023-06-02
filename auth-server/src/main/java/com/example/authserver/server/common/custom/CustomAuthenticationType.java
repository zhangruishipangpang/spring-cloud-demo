package com.example.authserver.server.common.custom;

/**
 * @author: 长安
 * 标记认证类型
 */
public interface CustomAuthenticationType {

    /**
     * 用户名密码
     */
    public static class UsernamePasswordType implements CustomAuthenticationType {}

    /**
     * 验证码
     */
    public static class verificationCodeType implements CustomAuthenticationType {}
}
