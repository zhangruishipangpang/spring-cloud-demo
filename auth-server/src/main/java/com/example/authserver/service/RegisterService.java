package com.example.authserver.service;

import com.example.authserver.model.register.mobile.MobileRegisterForm;
import com.example.authserver.model.register.mobile.MobileRegisterVo;

/**
 * @author: 长安
 */
public interface RegisterService {

    MobileRegisterVo mobileRegister(MobileRegisterForm form);
}
