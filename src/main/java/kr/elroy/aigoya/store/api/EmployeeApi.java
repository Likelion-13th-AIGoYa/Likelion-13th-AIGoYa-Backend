package kr.elroy.aigoya.store.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import kr.elroy.aigoya.store.dto.request.AddEmployeeRequest;
import kr.elroy.aigoya.store.dto.response.EmployeeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Tag(name = "직원 관리", description = "내 가게의 직원 관리 API (인증 필요)")
@RequestMapping("/v1/stores/me/employees")
public interface EmployeeApi {

    @Operation(summary = "직원 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EmployeeResponse createEmployee(
            @Parameter(hidden = true)
            @CurrentStoreId
            Long storeId,

            @Valid
            @RequestBody
            AddEmployeeRequest request
    );

    @Operation(summary = "직원 목록 조회")
    @GetMapping
    List<EmployeeResponse> getEmployees(
            @Parameter(hidden = true)
            @CurrentStoreId
            Long storeId
    );

    @Operation(summary = "직원 정보 수정")
    @PutMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateEmployee(
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId,

            @Parameter(description = "수정할 직원의 ID", required = true)
            @PathVariable
            Long employeeId,

            @Valid
            @RequestBody
            AddEmployeeRequest request
    );

    @Operation(summary = "직원 삭제")
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeEmployee(
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId,

            @Parameter(description = "삭제할 직원의 ID", required = true)
            @PathVariable
            Long employeeId
    );
}
