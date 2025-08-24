package kr.elroy.aigoya.analytics.controller;

import kr.elroy.aigoya.ai.dto.response.WeatherInfoResponse;
import kr.elroy.aigoya.analytics.api.AnalyticsApi;
import kr.elroy.aigoya.analytics.dto.request.AnalysisPeriod;
import kr.elroy.aigoya.analytics.dto.request.MenuAnalysisType;
import kr.elroy.aigoya.analytics.dto.response.DailySummaryResponse;
import kr.elroy.aigoya.analytics.dto.response.HourlySalesResponse;
import kr.elroy.aigoya.analytics.dto.response.MenuAnalysisResponse;
import kr.elroy.aigoya.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnalyticsController implements AnalyticsApi {
    private final AnalyticsService analyticsService;

    @Override
    public DailySummaryResponse getDailySummary(Long storeId, LocalDate date) {
        return analyticsService.getDailySummary(storeId, date);
    }

    @Override
    public List<HourlySalesResponse> getSalesByHour(Long storeId, LocalDate date) {
        return analyticsService.getSalesByHour(storeId, date);
    }

    @Override
    public List<MenuAnalysisResponse> getMenuAnalysis(Long storeId, MenuAnalysisType type, AnalysisPeriod period, Integer limit) {
        return analyticsService.getMenuAnalysis(storeId, type, period, limit);
    }

    @Override
    public WeatherInfoResponse getWeatherAnalytics(Long storeId) {
        return analyticsService.getWeatherAnalytics(storeId);
    }
}
