package kr.elroy.aigoya.store.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.elroy.aigoya.store.dto.request.CreateStoreRequest;
import kr.elroy.aigoya.store.dto.request.LoginRequest;
import kr.elroy.aigoya.store.dto.response.LoginResponse;
import kr.elroy.aigoya.store.dto.response.StoreResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "가게 (공개)", description = "로그인 없이 호출 가능한 가게 API")
@RestController
@Validated
@RequestMapping("/v1/stores")
public interface StoreApi {

    @Operation(summary = "가게 생성 (회원가입)", description = "새로운 가게를 생성합니다.")
    @PostMapping("/create")
    StoreResponse createStore(
            @Valid @RequestBody CreateStoreRequest request
    );

    @Operation(summary = "로그인", description = "가게 로그인을 합니다.")
    @PostMapping("/login")
    LoginResponse login(
            @Valid @RequestBody LoginRequest request
    );

    @Operation(summary = "특정 가게 정보 조회", description = "ID로 특정 가게의 공개 정보를 조회합니다.")
    @GetMapping("/{id}")
    StoreResponse getStore(
            @Parameter(description = "가게 ID", required = true, example = "1")
            @PathVariable Long id
    );

    // @Operation(summary = "내 가게 조회", ...) <-- 이 메서드와 관련된 선언을 삭제하세요.
    // StoreResponse getMyStore(); <-- 이 줄을 삭제하면 됩니다.
}