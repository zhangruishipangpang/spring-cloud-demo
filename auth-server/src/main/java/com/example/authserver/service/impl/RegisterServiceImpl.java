package com.example.authserver.service.impl;

import ca.commons.sms.SmsClient;
import ca.commons.util.RandomUtils;
import com.example.authserver.config.ex.RegisterException;
import com.example.authserver.general.entity.Oauth2ApplicationUser;
import com.example.authserver.general.repository.Oauth2ApplicationUserRepository;
import com.example.authserver.model.register.mobile.MobileRegisterForm;
import com.example.authserver.model.register.mobile.MobileRegisterVo;
import com.example.authserver.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import static java.lang.String.format;

/**
 * @author: 长安
 */
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    // for testing
    private final String DEFAULT_CODE = "123456";

    private final SmsClient smsClient;
    private final Oauth2ApplicationUserRepository userRepository;


    @Override
    public MobileRegisterVo mobileRegister(MobileRegisterForm form) {

        if(DEFAULT_CODE.equals(form.getSmsCode())) {
            // not check
        }

        // check mobile exist
        String mobile = form.getMobile();
        Oauth2ApplicationUser existUser = userRepository.findByMobile(mobile);
        if(Objects.nonNull(existUser)) {
            throw new RegisterException(
                format("%s 已经注册过，如忘记密码请尝试找回密码！", mobile)
            );
        }

        String username = RandomUtils.generateCode();

        Oauth2ApplicationUser newUser = new Oauth2ApplicationUser();
        newUser.setMobile(mobile);
        newUser.setUserName(username);

        Oauth2ApplicationUser saved = userRepository.save(newUser);

        return MobileRegisterVo.builder()
            .username(username)
            .nick(form.getNick())
            .mobile(saved.getMobile())
            .build();
    }
}
