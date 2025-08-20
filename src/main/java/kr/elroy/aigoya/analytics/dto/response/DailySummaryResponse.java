package kr.elroy.aigoya.analytics.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record DailySummaryResponse(
        @Schema(description = "조회 날짜")
        LocalDate date,

        @Schema(description = "총 매출액 (원)")
        long totalSales,

        @Schema(description = "거래 건수")
        int transactionCount,

        @Schema(description = "평균 객단가 (원)")
        long averageTransactionValue
) {}