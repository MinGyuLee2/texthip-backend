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

/**
 * OAuth2 로그인 성공 후, 소셜 서비스(예: Google)로부터 받은 사용자 정보를 처리하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    /**
     * 소셜 서비스로부터 사용자 정보를 로드합니다.
     * DB에 해당 사용자가 없으면 자동으로 회원가입을 처리합니다.
     * @param userRequest OAuth2 사용자 정보 요청
     * @return OAuth2User 객체
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 기본 OAuth2UserService를 통해 사용자 정보를 가져옵니다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. 사용자 정보에서 이메일과 이름을 추출합니다.
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String nickname = (String) attributes.get("name");

        // 3. DB에 이메일이 존재하는지 확인하고, 없으면 새로 저장 (자동 회원가입)
        userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .nickname(nickname)
                    .password("OAUTH_USER_PASSWORD") // 소셜 로그인 사용자는 비밀번호를 직접 사용하지 않으므로 임의의 값 설정
                    .build();
            return userRepository.save(newUser);
        });

        // 4. Spring Security가 내부적으로 사용할 OAuth2User 객체를 반환합니다.
        return oAuth2User;
    }
}