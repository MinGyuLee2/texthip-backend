package com.texthip.texthip_server.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // TODO: 향후 여기에 사용자 프로필 조회, 수정 등의 API를 추가합니다.
    // 예: @GetMapping("/me"), @PutMapping("/profile")
}