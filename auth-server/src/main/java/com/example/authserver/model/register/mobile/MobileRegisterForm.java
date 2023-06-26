package com.example.authserver.model.register.mobile;

import lombok.Data;

/**
 * @author: 长安
 */
@Data
public class MobileRegisterForm extends RegisterForm{
    /**
     * 电话号
     */
    private String mobile;
    /**
     * 短信验证码
     */
    private String smsCode;
}
