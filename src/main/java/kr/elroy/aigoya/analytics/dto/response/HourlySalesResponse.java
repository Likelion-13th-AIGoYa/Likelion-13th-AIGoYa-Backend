package kr.elroy.aigoya.analytics.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record HourlySalesResponse(
        @Schema(description = "시간 (0~23)")
        int hour,

        @Schema(description = "해당 시간대 매출액")
        long sales
) {}