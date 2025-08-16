package com.texthip.texthip_server.user;

import com.texthip.texthip_server.user.dto.OnboardingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserPreferredGenreRepository genreRepository;
    private final UserPreferredTopicRepository topicRepository;

    @Transactional
    public void saveOnboardingInfo(String userEmail, OnboardingRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        // UserProfile 정보 저장
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

        // 기존 선호 장르/주제는 삭제 후 새로 저장 (수정을 간편하게 하기 위함)
        genreRepository.deleteAll(user.getPreferredGenres());
        topicRepository.deleteAll(user.getPreferredTopics());

        // 새로운 선호 장르 저장
        requestDto.getPreferredGenres().forEach(genreName -> {
            UserPreferredGenre genre = new UserPreferredGenre(user, genreName);
            genreRepository.save(genre);
            user.getPreferredGenres().add(genre);
        });

        // 새로운 관심 주제 저장
        requestDto.getPreferredTopics().forEach(topicName -> {
            UserPreferredTopic topic = new UserPreferredTopic(user, topicName);
            topicRepository.save(topic);
            user.getPreferredTopics().add(topic);
        });
    }
}