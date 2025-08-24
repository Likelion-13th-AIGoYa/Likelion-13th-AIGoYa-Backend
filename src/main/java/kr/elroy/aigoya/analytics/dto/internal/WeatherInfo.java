package kr.elroy.aigoya.analytics.dto.internal;

import lombok.Builder;

@Builder
public record WeatherInfo(
        Double temperature,
        String summary
) {
}
