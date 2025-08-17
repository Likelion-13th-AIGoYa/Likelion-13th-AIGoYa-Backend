package kr.elroy.aigoya.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "가게 정보 수정 요청")
public record UpdateStoreRequest(
        @NotBlank(message = "가게 이름은 필수입니다.")
        @Schema(description = "새로운 가게 이름", example = "The New Elroy Store")
        String name,

        @NotBlank(message = "전화번호는 필수입니다.")
        @Schema(description = "새로운 전화번호", example = "010-9876-5432")
        String phone,

        @NotBlank(message = "주소는 필수입니다.")
        @Schema(description = "새로운 가게 주소", example = "부산시 해운대구 우동 1234")
        String address,

        @NotBlank(message = "일일 목표 매출은 필수입니다.")
        @Schema(description = "새로운 일일 목표 매출", example = "1200000")
        String dailyTarget
) {}