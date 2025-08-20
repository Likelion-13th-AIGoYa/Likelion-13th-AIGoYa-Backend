package kr.elroy.aigoya.analytics.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MenuAnalysisResponse(
        @Schema(description = "상품 ID")
        Long productId,

        @Schema(description = "상품명")
        String productName,

        @Schema(description = "총 판매 수량")
        long totalQuantity,

        @Schema(description = "총 판매 금액")
        long totalSales
) {}