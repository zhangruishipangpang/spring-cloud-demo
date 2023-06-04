package com.example.authserver.service.impl;

import com.example.authserver.model.verficationcode.KaptchaImage;
import com.example.authserver.server.common.custom.store.VerificationCodeStoreService;
import com.example.authserver.service.VerificationCodeService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * @author: 长安
 */
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private static final String IMAGE_FORMAT = "jpg";

    final Config config;
    final VerificationCodeStoreService verificationCodeStoreService;


    public VerificationCodeServiceImpl(Config config, VerificationCodeStoreService verificationCodeStoreService) {
        this.config = config;
        this.verificationCodeStoreService = verificationCodeStoreService;
    }


    @Override
    public void writeVerificationCode(KaptchaImage kaptchaImage, OutputStream outputStream) {
        Objects.requireNonNull(outputStream, "outputStream is null");
        Objects.requireNonNull(kaptchaImage, "kaptchaImage is null");
        Objects.requireNonNull(kaptchaImage.getBufferedImage(), "kaptchaImage#getBufferedImage() return null");

        try {
            ImageIO.write(kaptchaImage.getBufferedImage(), IMAGE_FORMAT, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public KaptchaImage createVerificationCode(String seed) {

        DefaultKaptcha producer = new DefaultKaptcha();
        producer.setConfig(config);

        String text = producer.createText();
        BufferedImage bufferedImage = producer.createImage(text);
        String id = UUID.randomUUID().toString();

        KaptchaImage kaptchaImage = KaptchaImage.builder()
            .id(id)
            .bufferedImage(bufferedImage)
            .text(text)
            .build();
        verificationCodeStoreService.setVerificationCode(kaptchaImage);

        return kaptchaImage;
    }
}
