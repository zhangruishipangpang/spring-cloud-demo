package com.example.authserver.server.common.custom.service;

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
 * @author 长安
 */
@Slf4j
@Component
public class MobileApplicationUserDetailsService implements UserDetailsService {

    final Oauth2ApplicationUserRepository oauth2ApplicationUserRepository;

    public MobileApplicationUserDetailsService(Oauth2ApplicationUserRepository oauth2ApplicationUserRepository) {
        this.oauth2ApplicationUserRepository = oauth2ApplicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        if(StringUtils.isBlank(mobile)) {
            throw new UsernameNotFoundException("mobile is blank!");
        }
        Oauth2ApplicationUser currentUser = oauth2ApplicationUserRepository.findByMobile(mobile);
        if(Objects.isNull(currentUser)) {
            throw new UsernameNotFoundException(String.format("can not find mobile [%s]", mobile));
        }
        if(log.isDebugEnabled()) {
            log.debug("[DEBUG]find current user -> {}", currentUser.toString());
        }
        return User.builder()
            .username(currentUser.getUserName())
            .password(currentUser.getPassword())
            // TODO 用户权限需要添加表字段
            .authorities(new SimpleGrantedAuthority("TEMP_ALL"))
            .build();
    }
}
