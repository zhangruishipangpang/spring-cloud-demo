package com.example.authserver.model.register.mobile;

import lombok.Builder;
import lombok.Data;

/**
 * @author: 长安
 */
@Data
@Builder
public class MobileRegisterVo {

    /**
     * 系统生成用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nick;

    /**
     * 手机号
     */
    private String mobile;
}
