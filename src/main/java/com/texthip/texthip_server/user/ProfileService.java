package com.texthip.texthip_server.user;

import com.texthip.texthip_server.user.dto.OnboardingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * 사용자 프로필 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * (온보딩 정보 저장 등)
 */
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserPreferredGenreRepository genreRepository;
    private final UserPreferredTopicRepository topicRepository;

    /**
     * 클라이언트로부터 받은 온보딩 정보를 사용자의 프로필, 선호 장르, 선호 주제에 저장합니다.
     * @param userEmail 정보를 저장할 사용자의 이메일
     * @param requestDto 온보딩 정보 DTO
     */
    @Transactional
    public void saveOnboardingInfo(String userEmail, OnboardingRequestDto requestDto) {
        // 1. 이메일을 기반으로 User 엔티티를 조회합니다.
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        // 2. User 엔티티와 연결된 UserProfile 정보를 업데이트합니다.
        UserProfile userProfile = user.getUserProfile();
        userProfile.updateProfile(
                requestDto.getAgeRange(),
                requestDto.getGender(),
                requestDto.getJob(),
                requestDto.getJobInfo(),
                requestDto.getReadingTime(),
                requestDto.getMonthlyReadingGoal()
        );
        userProfileRepository.save(userProfile);

        // 3. 기존에 저장되어 있던 선호 장르/주제는 모두 삭제하여 데이터 일관성을 유지합니다.
        //    (수정 시, 기존 항목과 새 항목을 비교하는 복잡한 로직 대신, 전체 삭제 후 새로 삽입하는 방식을 사용)
        genreRepository.deleteAll(user.getPreferredGenres());
        topicRepository.deleteAll(user.getPreferredTopics());
        // JPA의 영속성 컨텍스트 관리에 따라, 아래에서 user.getPreferredGenres()를 호출하기 전에
        // user 객체 내부의 컬렉션도 비워주는 것이 더 명확할 수 있습니다.
        user.getPreferredGenres().clear();
        user.getPreferredTopics().clear();


        // 4. DTO에 담겨온 새로운 선호 장르 목록을 저장합니다.
        requestDto.getPreferredGenres().forEach(genreName -> {
            UserPreferredGenre genre = new UserPreferredGenre(user, genreName);
            genreRepository.save(genre);
            user.getPreferredGenres().add(genre); // User 엔티티의 연관관계 컬렉션에도 추가
        });

        // 5. DTO에 담겨온 새로운 선호 주제 목록을 저장합니다.
        requestDto.getPreferredTopics().forEach(topicName -> {
            UserPreferredTopic topic = new UserPreferredTopic(user, topicName);
            topicRepository.save(topic);
            user.getPreferredTopics().add(topic); // User 엔티티의 연관관계 컬렉션에도 추가
        });
    }
}
