package kr.elroy.aigoya.common.weather.dto;

import lombok.Builder;

/**
 * WeatherService가 반환하는 날씨 정보 DTO
 */
@Builder
public record WeatherInfo(
        Double temperature, // 기온
        String summary      // 날씨 요약 ("맑음", "비" 등)
) {
}
