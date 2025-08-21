package kr.elroy.aigoya.analytics.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record HourlySalesResponse(
        @Schema(description = "시간", example = "10")
        Integer hour,

        @Schema(description = "매출액", example = "100000")
        Long totalSales
) {
}
