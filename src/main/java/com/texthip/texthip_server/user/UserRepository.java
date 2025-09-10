package com.texthip.texthip_server.user;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * User 엔티티에 대한 데이터베이스 연산을 처리하는 JpaRepository 인터페이스입니다.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 이메일 주소로 사용자를 조회합니다.
     * @param email 조회할 사용자의 이메일
     * @return Optional<User> 객체. 사용자가 존재하면 User 객체를, 아니면 빈 Optional을 반환합니다.
     */
    Optional<User> findByEmail(String email);

    /**
     * 닉네임으로 사용자를 조회합니다.
     * @param nickname 조회할 사용자의 닉네임
     * @return Optional<User> 객체.
     */
    Optional<User> findByNickname(String nickname);
}
