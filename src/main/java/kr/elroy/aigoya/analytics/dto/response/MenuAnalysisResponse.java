package kr.elroy.aigoya.analytics.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MenuAnalysisResponse(
        @Schema(description = "메뉴 이름", example = "피자")
        String menuName,

        @Schema(description = "카테고리 이름", example = "메인 메뉴")
        String categoryName,

        @Schema(description = "판매 수", example = "10")
        Long salesCount,

        @Schema(description = "매출액", example = "100000")
        Long totalSales
) {
}
