package com.example.authserver.model.register.mobile;

import lombok.Data;

/**
 * @author: 长安
 */
@Data
public abstract class RegisterForm {
    /*
        昵称
     */
    private String nick;
    /*
        邮箱
     */
    private String email;
    /*
        密码
     */
    private String password;
}
