package kr.elroy.aigoya.ai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메인 화면 날씨 위젯 응답")
public record WeatherInfoResponse(
        @Schema(description = "현재 또는 예보 온도", example = "15.5")
        Double temperature,

        @Schema(description = "날씨 요약 정보", example = "비")
        String weatherSummary,

        @Schema(description = "AI의 날씨 기반 판매 동향 분석", example = "비가 오는 날에는 따뜻한 국물 요리나 부침개류의 주문이 증가하는 경향이 있습니다.")
        String salesTrendInsight
) {
}