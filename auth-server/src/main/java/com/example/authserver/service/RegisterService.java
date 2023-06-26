package com.example.authserver.service;

import com.example.authserver.model.register.mobile.MobileRegisterForm;
import com.example.authserver.model.register.mobile.MobileRegisterVo;
import com.example.authserver.model.register.sms.SendSmsForm;

/**
 * @author: 长安
 */
public interface RegisterService {

    MobileRegisterVo mobileRegister(MobileRegisterForm form);

    Boolean sendSms(SendSmsForm form);
}
