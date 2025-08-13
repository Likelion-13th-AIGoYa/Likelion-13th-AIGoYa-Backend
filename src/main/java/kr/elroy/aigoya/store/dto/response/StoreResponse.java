package kr.elroy.aigoya.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.elroy.aigoya.store.Store;

@Schema(description = "가게 정보 응답")
public record StoreResponse(
        @Schema(description = "가게 ID", example = "1")
        Long id,

        @Schema(description = "가게 이메일", example = "me@elroy.kr")
        String email,

        @Schema(description = "가게 이름", example = "Elroy Store")
        String name
) {
    public static StoreResponse of(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getEmail(),
                store.getName()
        );
    }
}
