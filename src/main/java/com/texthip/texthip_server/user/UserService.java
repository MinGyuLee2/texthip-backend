package com.texthip.texthip_server.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 일반 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * (현재는 비어 있으며, 향후 기능 확장 예정)
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // TODO: 향후 여기에 사용자 프로필 조회, 수정 등의 비즈니스 로직을 추가합니다.
}
