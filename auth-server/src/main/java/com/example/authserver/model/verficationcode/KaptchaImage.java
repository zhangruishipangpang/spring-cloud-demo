package com.example.authserver.model.verficationcode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

/**
 * @author: 长安
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KaptchaImage {
    /*
        验证码唯一标识
     */
    private String id;

    /*
        验证码结果
     */
    private String text;

    /*
        验证码图片流
     */
    private BufferedImage bufferedImage;
}
