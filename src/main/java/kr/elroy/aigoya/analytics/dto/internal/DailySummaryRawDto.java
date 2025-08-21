package kr.elroy.aigoya.analytics.dto.internal;

public record DailySummaryRawDto(
        Long totalSales,
        Long orderCount
) {
    public DailySummaryRawDto {
        if (totalSales == null) {
            totalSales = 0L;
        }
        if (orderCount == null) {
            orderCount = 0L;
        }
    }
}
