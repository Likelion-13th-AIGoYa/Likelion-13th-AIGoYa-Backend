package kr.elroy.aigoya.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.elroy.aigoya.store.domain.Store;

@Schema(description = "가게 상세 정보 응답")
public record StoreResponse(
        @Schema(description = "가게 ID", example = "1")
        Long id,

        @Schema(description = "가게 이메일", example = "me@elroy.kr")
        String email,

        @Schema(description = "가게 이름", example = "Elroy Store")
        String name,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phone,

        @Schema(description = "가게 주소", example = "서울시 강남구 테헤란로 123")
        String address,

        @Schema(description = "일일 목표 매출", example = "1000000")
        Long dailyTarget
) {

    public static StoreResponse of(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getEmail(),
                store.getName(),
                store.getPhone(),
                store.getAddress(),
                store.getDailyTarget()
        );
    }
}