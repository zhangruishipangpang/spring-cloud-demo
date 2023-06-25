package com.example.authserver.model.user;

import lombok.Builder;
import lombok.Data;

/**
 * @author: 长安
 */
@Data
@Builder
public class UserMessageVo {

    /*
        用户名：系统唯一
     */
    private String username;
    /*
        email
     */
    private String email;
    /*
        昵称
     */
    private String nick;
    /*
        头像
     */
    private String headPortrait;
}
