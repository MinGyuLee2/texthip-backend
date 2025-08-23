package com.texthip.texthip_server.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // TODO: 향후 여기에 사용자 프로필 조회, 수정 등의 비즈니스 로직을 추가합니다.
}