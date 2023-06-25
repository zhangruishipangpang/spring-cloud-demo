package com.example.authserver.service;

import com.example.authserver.model.user.UserMessageForm;
import com.example.authserver.model.user.UserMessageVo;

/**
 * @author: 长安
 */
public interface UserService {

    UserMessageVo getUserMessage(UserMessageForm form);
}
