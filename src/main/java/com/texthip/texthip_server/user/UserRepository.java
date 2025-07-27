package com.texthip.texthip_server.user;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 사용자를 찾기 위한 메소드
    Optional<User> findByEmail(String email);
    // 닉네임으로 사용자를 찾기 위한 메소드
    Optional<User> findByNickname(String nickname);
}