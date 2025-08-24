package kr.elroy.aigoya.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import kr.elroy.aigoya.analytics.dto.internal.WeatherInfo;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WebClient.Builder webClientBuilder;
    private final StoreRepository storeRepository;

    @Value("${kma.api.service-key}")
    private String serviceKey;

    @Value("${kma.api.url}")
    private String apiUrl;

    private static final int DEFAULT_NX = 60;
    private static final int DEFAULT_NY = 127;
    private static final String DEFAULT_WEATHER_SUMMARY = "정보 없음";
    private static final Double DEFAULT_TEMPERATURE = null;

    public WeatherInfo getWeatherFor(LocalDate date, int nx, int ny) {
        String baseDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = "0500";

        try {
            WebClient webClient = webClientBuilder.build();
            JsonNode response = webClient.get()
                    .uri(apiUrl, uriBuilder -> uriBuilder
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("pageNo", 1)
                            .queryParam("numOfRows", 1000)
                            .queryParam("dataType", "JSON")
                            .queryParam("base_date", baseDate)
                            .queryParam("base_time", baseTime)
                            .queryParam("nx", nx)
                            .queryParam("ny", ny)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            return parseWeatherFromResponse(response, baseDate);
        } catch (Exception e) {
            log.error("기상청 데이터 조회 실패. nx={}, ny={}", nx, ny, e);
            return WeatherInfo.builder().summary(DEFAULT_WEATHER_SUMMARY).temperature(DEFAULT_TEMPERATURE).build();
        }
    }

    private WeatherInfo parseWeatherFromResponse(JsonNode response, String targetDate) {
        JsonNode items = response.path("response").path("body").path("items").path("item");
        if (!items.isArray()) {
            JsonNode header = response.path("response").path("header");
            log.warn("KMA API 응답 형식이 잘못되었거나 오류가 포함됨. resultCode: {}, resultMsg: {}, header: {}",
                    header.path("resultCode").asText(), header.path("resultMsg").asText(), header.toString());
            return WeatherInfo.builder().summary(DEFAULT_WEATHER_SUMMARY).temperature(DEFAULT_TEMPERATURE).build();
        }

        String summary = StreamSupport.stream(items.spliterator(), false)
                .filter(item -> Objects.equals(item.path("fcstDate").asText(), targetDate))
                .filter(item -> "PTY".equals(item.path("category").asText()))
                .map(item -> item.path("fcstValue").asInt())
                .filter(ptyCode -> ptyCode > 0)
                .findFirst()
                .map(this::translatePtyCode)
                .orElseGet(() -> StreamSupport.stream(items.spliterator(), false)
                        .filter(item -> Objects.equals(item.path("fcstDate").asText(), targetDate))
                        .filter(item -> "SKY".equals(item.path("category").asText()))
                        .filter(item -> "1200".equals(item.path("fcstTime").asText()))
                        .findFirst()
                        .map(item -> item.path("fcstValue").asInt())
                        .map(this::translateSkyCode)
                        .orElse(DEFAULT_WEATHER_SUMMARY));

        Double temperature = StreamSupport.stream(items.spliterator(), false)
                .filter(item -> Objects.equals(item.path("fcstDate").asText(), targetDate))
                .filter(item -> "T1H".equals(item.path("category").asText()))
                .findFirst()
                .map(item -> item.path("fcstValue").asDouble())
                .orElse(DEFAULT_TEMPERATURE);

        return WeatherInfo.builder().summary(summary).temperature(temperature).build();
    }

    private String translatePtyCode(int code) {
        return switch (code) {
            case 1 -> "비";
            case 2 -> "비/눈";
            case 3 -> "눈";
            case 4 -> "소나기";
            default -> null;
        };
    }

    private String translateSkyCode(int code) {
        return switch (code) {
            case 1 -> "맑음";
            case 3 -> "구름많음";
            case 4 -> "흐림";
            default -> null;
        };
    }

    public WeatherInfo getWeatherForStore(Long storeId, LocalDate date) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store Id: " + storeId));

        log.info("가게 날씨 조회: storeId={}, nx={}, ny={}", storeId, store.getNx(), store.getNy());

        int nx = store.getNx() != null ? store.getNx() : DEFAULT_NX;
        int ny = store.getNy() != null ? store.getNy() : DEFAULT_NY;

        return getWeatherFor(date, nx, ny);
    }
}
