package kr.elroy.aigoya.analytics.dto.internal;

import lombok.Builder;

/**
 * WeatherService가 반환하는, 서비스 내부 통신용 날씨 정보 DTO
 */
@Builder
public record WeatherInfo(
        Double temperature, // 기온
        String summary      // 날씨 요약 ("맑음", "비" 등)
) {
}
