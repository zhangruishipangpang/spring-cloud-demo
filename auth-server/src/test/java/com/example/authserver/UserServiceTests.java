package com.example.authserver;

import com.example.authserver.general.entity.Oauth2ApplicationUser;
import com.example.authserver.general.entity.Oauth2RegisteredClientEntity;
import com.example.authserver.general.repository.Oauth2ApplicationUserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Auther: 长安
 */
@Slf4j
@SpringBootTest
public class UserServiceTests {

    @Resource
    Oauth2ApplicationUserRepository oauth2ApplicationUserRepository;

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

}
