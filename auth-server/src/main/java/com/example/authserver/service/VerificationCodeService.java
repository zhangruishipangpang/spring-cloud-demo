package com.example.authserver.service;

import com.example.authserver.model.verficationcode.KaptchaImage;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author: 长安
 */
public interface VerificationCodeService {

    void writeVerificationCode(KaptchaImage kaptchaImage, OutputStream outputStream);

    KaptchaImage createVerificationCode(String seed);
}
