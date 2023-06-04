package com.example.authserver.view;

import com.example.authserver.model.verficationcode.KaptchaImage;
import com.example.authserver.service.VerificationCodeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: 长安
 */
@RestController
@RequestMapping("/verification_code")
public class VerificationCodeController {

    @Resource
    VerificationCodeService verificationCodeService;

    @GetMapping("/get")
    public Map<String, String> getVerificationCodeForTesting() {
        KaptchaImage verificationCode = verificationCodeService.createVerificationCode("");
        return Map.of(
            "text", verificationCode.getText(),
            "id", verificationCode.getId()
        );
    }
}
