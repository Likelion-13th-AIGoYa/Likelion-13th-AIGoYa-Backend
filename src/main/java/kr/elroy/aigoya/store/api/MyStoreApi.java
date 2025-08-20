package kr.elroy.aigoya.store.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import kr.elroy.aigoya.store.dto.request.UpdatePasswordRequest;
import kr.elroy.aigoya.store.dto.request.UpdateStoreRequest;
import kr.elroy.aigoya.store.dto.response.StoreResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "내 가게 관리", description = "로그인한 사용자의 가게 정보 관리 API (인증 필요)")
@Validated
@RequestMapping("/v1/stores/me")
public interface MyStoreApi {

    @Operation(summary = "내 가게 정보 조회", description = "로그인한 사용자의 가게 정보를 조회합니다.")
    @GetMapping
    StoreResponse getMyStore(
            @Parameter(hidden = true)
            @CurrentStoreId
            Long storeId
    );

    @Operation(summary = "내 가게 정보 수정", description = "로그인한 사용자의 가게 정보를 수정합니다.")
    @PutMapping
    StoreResponse updateMyStore(
            @Parameter(hidden = true)
            @CurrentStoreId
            Long storeId,

            @Valid
            @RequestBody
            UpdateStoreRequest request
    );

    @Operation(summary = "내 가게 삭제 (탈퇴)", description = "로그인한 사용자의 가게 정보를 삭제하고 탈퇴 처리합니다.")
    @DeleteMapping
    void deleteMyStore(
            @Parameter(hidden = true)
            @CurrentStoreId
            Long storeId
    );

    @PutMapping("/v1/stores/me/password")
    public void updatePassword(
            @CurrentStoreId
            Long storeId,

            @Valid
            @RequestBody
            UpdatePasswordRequest request
    );
}