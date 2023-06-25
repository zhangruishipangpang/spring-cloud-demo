package com.example.authserver.service.impl;

import com.example.authserver.config.ex.AuthServerException;
import com.example.authserver.general.entity.Oauth2ApplicationUser;
import com.example.authserver.general.entity.UserInformationMessage;
import com.example.authserver.general.repository.Oauth2ApplicationUserRepository;
import com.example.authserver.general.repository.UserInformationMessageRepository;
import com.example.authserver.model.user.UserMessageForm;
import com.example.authserver.model.user.UserMessageVo;
import com.example.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

/**
 * @author: 长安
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Oauth2ApplicationUserRepository userRepository;
    private final UserInformationMessageRepository userInformationRepository;

    @Override
    public UserMessageVo getUserMessage(UserMessageForm form) {
        Authentication authed = SecurityContextHolder.getContext().getAuthentication();
        String username =authed.getName();

        Oauth2ApplicationUser dbUser = userRepository.findByUserName(username);

        if(Objects.isNull(dbUser)) {
            throw new AuthServerException(format("用户[%s]不存在！", username));
        }

        UserMessageVo.UserMessageVoBuilder builder = UserMessageVo.builder().username(username);
        if(Objects.isNull(dbUser.getUid())) {
            return builder.build();
        }

        Optional<UserInformationMessage> userInformation = userInformationRepository.findById(dbUser.getUid());

        if(userInformation.isPresent()) {
            return builder
                .nick(userInformation.get().getNick())
                .email(userInformation.get().getEmail())
                .headPortrait(userInformation.get().getHeadPortrait())
                .build();
        }

        return builder.build();
    }
}
