package kr.elroy.aigoya.analytics.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.elroy.aigoya.analytics.dto.request.AnalysisPeriod;
import kr.elroy.aigoya.analytics.dto.request.MenuAnalysisType;
import kr.elroy.aigoya.analytics.dto.response.DailySummaryResponse;
import kr.elroy.aigoya.analytics.dto.response.HourlySalesResponse;
import kr.elroy.aigoya.analytics.dto.response.MenuAnalysisResponse;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "애널리틱스", description = "내 가게의 데이터 분석 API (인증 필요)")
@Validated
@RequestMapping("/v1/stores/me/analytics")
public interface AnalyticsApi {
    @Operation(summary = "일일 핵심 지표 요약", description = "특정 날짜의 총 매출, 거래 건수, 평균 객단가를 조회합니다.")
    @GetMapping("/daily-summary")
    DailySummaryResponse getDailySummary(
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId,

            @Parameter(description = "조회할 날짜 (YYYY-MM-DD). 미입력 시 오늘 날짜.")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    );

    @Operation(summary = "시간대별 매출 분석", description = "특정 날짜의 시간대별 매출 분포를 조회합니다.")
    @GetMapping("/sales-by-hour")
    List<HourlySalesResponse> getSalesByHour(
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId,

            @Parameter(description = "조회할 날짜 (YYYY-MM-DD). 미입력 시 오늘 날짜.")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    );

    @Operation(summary = "메뉴 분석 (인기/비인기)", description = "기간별 인기 또는 비인기 메뉴 순위를 조회합니다.")
    @GetMapping("/menu-analysis")
    List<MenuAnalysisResponse> getMenuAnalysis(
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId,

            @Parameter(description = "분석 타입 (TOP: 인기, BOTTOM: 비인기)", required = true, example = "TOP")
            @RequestParam
            MenuAnalysisType type,

            @Parameter(description = "조회 기간 (DAILY: 오늘, WEEKLY: 이번 주)", required = true, example = "DAILY")
            @RequestParam
            AnalysisPeriod period,

            @Parameter(description = "조회할 메뉴 개수")
            @RequestParam(defaultValue = "5")
            Integer limit
    );
}