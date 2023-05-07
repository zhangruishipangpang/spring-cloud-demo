package com.example.authserver.server.auth.custom;

import com.example.authserver.general.entity.Oauth2ApplicationUser;
import com.example.authserver.general.repository.Oauth2ApplicationUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Auther: 长安
 */
@Slf4j
@Component
public class ApplicationUserDetailsService implements UserDetailsService {

    final Oauth2ApplicationUserRepository oauth2ApplicationUserRepository;

    public ApplicationUserDetailsService(Oauth2ApplicationUserRepository oauth2ApplicationUserRepository) {
        this.oauth2ApplicationUserRepository = oauth2ApplicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("username is blank!");
        }
        Oauth2ApplicationUser currentUser = oauth2ApplicationUserRepository.findByUserName(username);
        if(Objects.isNull(currentUser)) {
            throw new UsernameNotFoundException(String.format("can not find username [%s]", username));
        }
        log.info("find current user -> {}", currentUser.toString());
        return User.builder()
            .username(currentUser.getUserName())
            .password(currentUser.getPassword())
            .authorities(new SimpleGrantedAuthority("TEMP_ALL"))
            .build();
    }
}
