package com.example.authserver.server.common.custom.store;

import com.example.authserver.model.verficationcode.KaptchaImage;

/**
 * @author: 长安
 */
public interface VerificationCodeStoreService {

    KaptchaImage setVerificationCode(KaptchaImage kaptchaImage);

    boolean hasVerificationCode(String verificationId);

    String getVerificationCode(String verificationId);

    boolean evictVerificationCode(String verificationId);
}
