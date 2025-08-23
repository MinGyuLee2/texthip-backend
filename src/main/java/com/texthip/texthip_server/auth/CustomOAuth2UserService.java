package com.texthip.texthip_server.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.texthip.texthip_server.user.User;
import com.texthip.texthip_server.user.UserRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String nickname = (String) attributes.get("name");

        // DB에 사용자가 없으면 새로 저장 (자동 회원가입)
        userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .nickname(nickname)
                    .password("OAUTH_USER_PASSWORD") // 소셜 로그인 사용자는 비밀번호를 사용하지 않음
                    .build();
            return userRepository.save(newUser);
        });

        return oAuth2User;
    }
}
