package kr.elroy.aigoya.store;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.elroy.aigoya.store.dto.CreateStoreRequest;
import kr.elroy.aigoya.store.dto.LoginRequest;
import kr.elroy.aigoya.store.dto.LoginResponse;
import kr.elroy.aigoya.store.dto.StoreResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "가게 API", description = "가게 API")
@RestController
@Validated
@RequestMapping("/v1/stores")
public interface StoreApi {

    @Operation(summary = "가게 조회", description = "가게 정보를 조회합니다.")
    @GetMapping("/{id}")
    StoreResponse getStore(
            @Parameter(description = "가게 ID", required = true, example = "1")
            @PathVariable
            Long id
    );

    @Operation(summary = "가게 생성", description = "새로운 가게를 생성합니다.")
    @PostMapping("/create")
    StoreResponse createStore(
            @Valid
            @RequestBody
            @Parameter(description = "가게 정보", required = true)
            CreateStoreRequest request
    );

    @Operation(summary = "로그인", description = "가게 로그인을 합니다.")
    @PostMapping("/login")
    LoginResponse login(
            @Valid
            @RequestBody
            @Parameter(description = "로그인 정보", required = true)
            LoginRequest request
    );
}
