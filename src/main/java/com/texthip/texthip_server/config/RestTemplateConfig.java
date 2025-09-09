package com.texthip.texthip_server.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 외부 API와의 동기 방식 HTTP 통신을 위한 RestTemplate 빈을 설정하는 클래스입니다.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 애플리케이션 전역에서 사용할 RestTemplate 객체를 스프링 빈으로 등록합니다.
     * @param builder 스프링 부트가 자동으로 구성해주는 RestTemplateBuilder
     * @return 설정이 완료된 RestTemplate 객체
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
