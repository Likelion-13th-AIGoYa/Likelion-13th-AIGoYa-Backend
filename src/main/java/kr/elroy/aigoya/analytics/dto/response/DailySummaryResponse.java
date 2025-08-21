package kr.elroy.aigoya.analytics.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record DailySummaryResponse(
        @Schema(description = "총 매출액", example = "100000")
        Long totalSales,

        @Schema(description = "총 주문 수", example = "10")
        Long orderCount,

        @Schema(description = "객단가", example = "10000")
        Long averageOrderValue
) {
}
