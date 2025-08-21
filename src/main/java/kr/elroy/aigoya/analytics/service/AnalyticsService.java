package kr.elroy.aigoya.analytics.service;

import kr.elroy.aigoya.analytics.dto.internal.DailySummaryRawDto;
import kr.elroy.aigoya.analytics.dto.request.AnalysisPeriod;
import kr.elroy.aigoya.analytics.dto.request.MenuAnalysisType;
import kr.elroy.aigoya.analytics.dto.response.DailySummaryResponse;
import kr.elroy.aigoya.analytics.dto.response.HourlySalesResponse;
import kr.elroy.aigoya.analytics.dto.response.MenuAnalysisResponse;
import kr.elroy.aigoya.analytics.exception.InvalidAnalysisParameterException;
import kr.elroy.aigoya.analytics.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnalyticsService {

    private static final int MAX_MENU_ANALYSIS_LIMIT = 50;

    private final AnalyticsRepository analyticsRepository;

    public DailySummaryResponse getDailySummary(Long storeId, LocalDate date) {
        LocalDate targetDate = (date == null) ? LocalDate.now() : date;
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

        DailySummaryRawDto rawDto = analyticsRepository.findDailySummaryRaw(storeId, startOfDay, endOfDay);

        Long totalSales = rawDto.totalSales();
        Long orderCount = rawDto.orderCount();
        long averageOrderValue = (orderCount == 0) ? 0 : totalSales / orderCount;

        return DailySummaryResponse.builder()
                .totalSales(totalSales)
                .orderCount(orderCount)
                .averageOrderValue(averageOrderValue)
                .build();
    }

    public List<HourlySalesResponse> getSalesByHour(Long storeId, LocalDate date) {
        LocalDate targetDate = (date == null) ? LocalDate.now() : date;
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

        List<HourlySalesResponse> salesByHour = analyticsRepository.findHourlySales(storeId, startOfDay, endOfDay);
        Map<Integer, Long> salesMap = salesByHour.stream()
                .collect(Collectors.toMap(HourlySalesResponse::hour, HourlySalesResponse::totalSales));

        return IntStream.range(0, 24)
                .mapToObj(hour -> HourlySalesResponse.builder()
                        .hour(hour)
                        .totalSales(salesMap.getOrDefault(hour, 0L))
                        .build())
                .collect(Collectors.toList());
    }

    public List<MenuAnalysisResponse> getMenuAnalysis(Long storeId, MenuAnalysisType type, AnalysisPeriod period, Integer limit) {
        if (limit <= 0 || limit > MAX_MENU_ANALYSIS_LIMIT) {
            throw new InvalidAnalysisParameterException();
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;

        if (period == AnalysisPeriod.DAILY) {
            startDate = now.toLocalDate().atStartOfDay();
        } else {
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            startDate = now.with(weekFields.dayOfWeek(), 1).toLocalDate().atStartOfDay();
        }

        Pageable pageable = PageRequest.of(0, limit);

        if (type == MenuAnalysisType.TOP) {
            return analyticsRepository.findMenuAnalysisTop(storeId, startDate, now, pageable).getContent();
        } else {
            return analyticsRepository.findMenuAnalysisBottom(storeId, startDate, now, pageable).getContent();
        }
    }
}
