package com.example.authserver;

import com.example.authserver.general.entity.Oauth2ApplicationUser;
import com.example.authserver.general.entity.Oauth2RegisteredClientEntity;
import com.example.authserver.general.entity.UserInformationMessage;
import com.example.authserver.general.repository.Oauth2ApplicationUserRepository;
import com.example.authserver.general.repository.UserInformationMessageRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Auther: 长安
 */
@Slf4j
@SpringBootTest
public class UserServiceTests {

    @Resource
    Oauth2ApplicationUserRepository oauth2ApplicationUserRepository;
    @Resource
    UserInformationMessageRepository userInformationMessageRepository;

    @Test
    void test_findUser() {

        Oauth2ApplicationUser user = oauth2ApplicationUserRepository.findByUserName("user1");

        assertTrue(new BCryptPasswordEncoder().matches( "secret", user.getPassword()));
        assertTrue(user != null);
        log.info("user -> {}", user);
    }

    @Test
    void test_insertUser() {

        Oauth2ApplicationUser user = new Oauth2ApplicationUser();
        user.setUserName("user1");
        user.setPassword(new BCryptPasswordEncoder().encode("secret"));
        Oauth2ApplicationUser saveUser = oauth2ApplicationUserRepository.save(user);

        assertTrue(saveUser != null);
        log.info("saveUser -> {}", saveUser);
    }

    @Test
    void test_userInformation() {

        Oauth2ApplicationUser user = oauth2ApplicationUserRepository.findByUserName("user1");
        Optional<UserInformationMessage> information = userInformationMessageRepository.findById(user.getId());

        log.info("user information -> {}", information);
    }

}
