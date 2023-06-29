package com.example.authserver.view;

import ca.commons.response.ResponseBody;
import com.example.authserver.model.register.mobile.MobileRegisterForm;
import com.example.authserver.model.register.mobile.MobileRegisterVo;
import com.example.authserver.model.register.sms.SendSmsForm;
import com.example.authserver.service.RegisterService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 长安
 * 注册接口组
 */
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Resource
    private RegisterService registerService;


    @PostMapping("/mobile")
    public ResponseBody<MobileRegisterVo> registerNew(@RequestBody @Validated MobileRegisterForm form) {
        return ResponseBody.success(
            registerService.mobileRegister(form)
        );
    }

    @PostMapping("/sms")
    public ResponseBody<Boolean> sendSms(SendSmsForm form) {
        return ResponseBody.success();
    }
}
