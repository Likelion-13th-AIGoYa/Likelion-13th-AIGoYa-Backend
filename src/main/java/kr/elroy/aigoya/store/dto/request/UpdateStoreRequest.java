package kr.elroy.aigoya.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가게 정보 수정 요청 (변경하려는 필드만 포함)")
public record UpdateStoreRequest(

        @Schema(description = "새로운 전화번호", example = "010-9876-5432")
        String phone,

        @Schema(description = "새로운 가게 이름", example = "The New Elroy Store")
        String name,

        @Schema(description = "새로운 가게 주소", example = "부산시 해운대구 우동 1234")
        String address,

        @Schema(description = "새로운 일일 목표 매출", example = "1200000")
        Long dailyTarget
) {
}
