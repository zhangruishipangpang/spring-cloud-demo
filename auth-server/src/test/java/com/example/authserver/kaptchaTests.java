package com.example.authserver;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;

/**
 * @author: 长安
 */
@Slf4j
@SpringBootTest
public class kaptchaTests {

    @Test
    void test_create() throws IOException {
        // 配置图形验证码的基本参数
        Properties properties = new Properties();
        // 图片宽度
        properties.setProperty("kaptcha.image.width", "150");
        // 图片长度
        properties.setProperty("kaptcha.image.height", "50");
        // 字符集
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        // 字符长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        Config config = new Config(properties);


        DefaultKaptcha producer = new DefaultKaptcha();
        producer.setConfig(new Config(properties));

        String text = producer.createText();
        log.info("createText  ->  {}", text);

        BufferedImage image = producer.createImage(text);
        OutputStream os = new FileOutputStream(new File("ttt.jpg"));
        ImageIO.write(image, "jpg", os);
        log.info("done.");

        ObjectOutputStream oos = new ObjectOutputStream(os);
    }

    @Test
    void test_null() {
        System.out.println(null instanceof String);
    }
}
