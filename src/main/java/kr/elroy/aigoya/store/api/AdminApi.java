package kr.elroy.aigoya.store.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.elroy.aigoya.store.dto.response.StoreResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "가게 관리", description = "관리자 권한이 필요한 가게 관리 API")
@RestController
@RequestMapping("/v1/stores/admin")
public interface AdminApi {

    @Operation(summary = "특정 가게 정보 조회", description = "ID로 특정 가게의 모든 정보를 조회합니다.")
    @GetMapping("/{id:[0-9]+}")
    StoreResponse getStore(
            @Parameter(description = "가게 ID", required = true, example = "1")
            @PathVariable Long id
    );
}