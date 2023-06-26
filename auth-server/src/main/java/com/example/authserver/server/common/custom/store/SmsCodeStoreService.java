package com.example.authserver.server.common.custom.store;

import com.example.authserver.model.verficationcode.KaptchaImage;

/**
 * @author: 长安
 */
public interface SmsCodeStoreService {

    void setSmsCode(String mobile, String smsCode);

    boolean hasSmsCode(String mobile);

    String getSmsCode(String mobile);

    boolean evictSmsCode(String mobile);
}
