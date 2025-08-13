package kr.elroy.aigoya.store.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import kr.elroy.aigoya.store.dto.response.StoreResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "나의 가게 API", description = "나의 가게 API")
@Validated
@RequestMapping("/v1/stores/me")
public interface MyStoreApi {
    @Operation(summary = "나의 가게 정보 조회", description = "현재 토큰 주인의 정보를 조회합니다.")
    @GetMapping
    StoreResponse getMyStore(
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "탈퇴", description = "현재 토큰 주인이 탈퇴합니다.")
    @DeleteMapping
    void deleteMyStore(
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );
}
