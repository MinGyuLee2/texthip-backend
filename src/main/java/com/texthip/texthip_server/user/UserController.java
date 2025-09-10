package com.texthip.texthip_server.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * '/api/users' 경로의 일반 사용자 관련 API 요청을 처리하는 컨트롤러입니다.
 * (현재는 비어 있으며, 향후 기능 확장 예정)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // TODO: 향후 여기에 사용자 자신의 프로필 조회, 수정 등의 API를 추가합니다.
    // 예: @GetMapping("/me"), @PutMapping("/profile")
}
