package com.example.authserver.server.common.custom;

/**
 * @author: 长安
 * 标记认证类型
 */
public interface CustomAuthenticationType {

    /**
     * 用户名密码
     * e.g. 暂时不用配置，默认一定需要用户名密码，其他的则为多因子认证扩展项
     */
    public static class UsernamePasswordType implements CustomAuthenticationType {}

    /**
     * 验证码
     */
    public static class verificationCodeType implements CustomAuthenticationType {}
}
