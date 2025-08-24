package kr.elroy.aigoya.analytics.service;

import kr.elroy.aigoya.ai.dto.response.WeatherInfoResponse;
import kr.elroy.aigoya.ai.service.AiService;
import kr.elroy.aigoya.ai.service.WeatherService;
import kr.elroy.aigoya.analytics.dto.internal.WeatherInfo;
import kr.elroy.aigoya.analytics.dto.internal.DailySummaryRawDto;
import kr.elroy.aigoya.analytics.dto.request.AnalysisPeriod;
import kr.elroy.aigoya.analytics.dto.request.MenuAnalysisType;
import kr.elroy.aigoya.analytics.dto.response.DailySummaryResponse;
import kr.elroy.aigoya.analytics.dto.response.HourlySalesResponse;
import kr.elroy.aigoya.analytics.dto.response.MenuAnalysisResponse;
import kr.elroy.aigoya.analytics.exception.InvalidAnalysisParameterException;
import kr.elroy.aigoya.analytics.repository.AnalyticsRepository;
import kr.elroy.aigoya.product.domain.Product;
import kr.elroy.aigoya.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnalyticsService {

    private static final int MAX_MENU_ANALYSIS_LIMIT = 50;

    private final AnalyticsRepository analyticsRepository;
    private final WeatherService weatherService;
    private final AiService aiService;
    private final ProductRepository productRepository;

    @Cacheable(value = "weatherAnalytics", key = "#storeId")
    public WeatherInfoResponse getWeatherAnalytics(Long storeId) {
        log.info("캐시된 데이터가 없어 새로운 날씨 기반 분석 정보를 생성합니다. storeId: {}", storeId);

        // ★★★ [수정] 빠져있던 날짜 파라미터를 추가하여 호출 ★★★
        WeatherInfo weatherInfo = weatherService.getWeatherForStore(storeId, LocalDate.now());
        String weatherSummary = weatherInfo.summary();
        Double temperature = weatherInfo.temperature();

        String weatherDataForAi;
        if (temperature != null) {
            weatherDataForAi = String.format("현재 날씨: %s, 기온: %.1f도", weatherSummary, temperature);
        } else {
            weatherDataForAi = String.format("현재 날씨: %s, 기온 정보 없음", weatherSummary);
        }

        List<Product> products = productRepository.findByStoreId(storeId);
        String productListForAi = products.stream()
                .map(Product::getName)
                .collect(Collectors.joining(", "));

        if (productListForAi.isEmpty()) {
            productListForAi = "등록된 상품 없음";
        }

        String salesTrendInsight = aiService.generateWeatherBasedSalesTrend(weatherDataForAi, productListForAi);

        return new WeatherInfoResponse(temperature, weatherSummary, salesTrendInsight);
    }

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
