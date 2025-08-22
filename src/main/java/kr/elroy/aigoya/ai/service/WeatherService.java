package kr.elroy.aigoya.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
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

    public String getWeatherFor(LocalDate date, int nx, int ny) {
        String baseDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = "0500";

        try {
            WebClient webClient = webClientBuilder.build();
            Mono<JsonNode> responseMono = webClient.get()
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
                    .bodyToMono(JsonNode.class);

            JsonNode response = responseMono.block();
            return parseWeatherFromResponse(response, baseDate);
        } catch (Exception e) {
            log.error("Failed to fetch weather data for nx={}, ny={}", nx, ny, e);
            return "날씨 정보 없음";
        }
    }

    private String parseWeatherFromResponse(JsonNode response, String targetDate) {
        JsonNode items = response.path("response").path("body").path("items").path("item");
        if (!items.isArray()) {
            JsonNode header = response.path("response").path("header");
            String resultCode = header.path("resultCode").asText();
            String resultMsg = header.path("resultMsg").asText();
            log.warn("KMA API response format is not as expected or contains an error. resultCode: {}, resultMsg: {}, header: {}",
                    resultCode, resultMsg, header.toString());
            return "날씨 정보 없음";
        }

        String precipitation = StreamSupport.stream(items.spliterator(), false)
                .filter(item -> Objects.equals(item.path("fcstDate").asText(), targetDate))
                .filter(item -> "PTY".equals(item.path("category").asText()))
                .map(item -> item.path("fcstValue").asInt())
                .filter(ptyCode -> ptyCode > 0)
                .findFirst()
                .map(this::translatePtyCode)
                .orElse(null);

        if (precipitation != null) {
            return precipitation;
        }

        return StreamSupport.stream(items.spliterator(), false)
                .filter(item -> Objects.equals(item.path("fcstDate").asText(), targetDate))
                .filter(item -> "SKY".equals(item.path("category").asText()))
                .filter(item -> "1200".equals(item.path("fcstTime").asText()))
                .findFirst()
                .map(item -> item.path("fcstValue").asInt())
                .map(this::translateSkyCode)
                .orElse("맑음");
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

    public String getWeatherForStore(Long storeId, LocalDate date) {
        Optional<Store> storeOptional = storeRepository.findById(storeId);

        int nx = storeOptional.map(Store::getNx).orElse(DEFAULT_NX);
        int ny = storeOptional.map(Store::getNy).orElse(DEFAULT_NY);

        log.info("Fetching weather for storeId: {} (nx={}, ny={})", storeId, nx, ny);

        return getWeatherFor(date, nx, ny);
    }
}
