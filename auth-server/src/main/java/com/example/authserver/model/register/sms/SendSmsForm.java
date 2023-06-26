package com.example.authserver.model.register.sms;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author: 长安
 */
@Data
public class SendSmsForm {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号为空")
    private String mobile;
}
