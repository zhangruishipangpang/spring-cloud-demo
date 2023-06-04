package com.example.authserver.server.common.custom.store;

import com.example.authserver.model.verficationcode.KaptchaImage;
import com.example.authserver.service.VerificationCodeService;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: 长安
 */
public class VerificationCodeStoreServiceImpl
    extends HashMap<String, KaptchaImage> implements VerificationCodeStoreService {


    @Override
    public KaptchaImage setVerificationCode(KaptchaImage kaptchaImage) {
        Objects.requireNonNull(kaptchaImage, "kaptchaImage is null");
        return put(kaptchaImage.getId(), kaptchaImage);
    }

    @Override
    public boolean hasVerificationCode(String verificationId) {
        return containsKey(verificationId);
    }

    @Override
    public String getVerificationCode(String verificationId) {
        KaptchaImage kaptchaImage = get(verificationId);
        if(Objects.isNull(kaptchaImage)) {
            return null;
        }
        return kaptchaImage.getText();
    }

    @Override
    public boolean evictVerificationCode(String verificationId) {

        return remove(verificationId) != null;
    }
}
