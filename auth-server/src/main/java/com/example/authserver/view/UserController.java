package com.example.authserver.view;

import ca.commons.response.ResponseBody;
import com.example.authserver.model.user.UserMessageForm;
import com.example.authserver.model.user.UserMessageVo;
import com.example.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 长安
 * 系统资源接口
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/userMessage")
    public ResponseBody<UserMessageVo> getUserMessage(@RequestBody @Validated UserMessageForm form) {
        return ResponseBody.success(
            userService.getUserMessage(form)
        );
    }
}
