package kr.elroy.aigoya.store.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.elroy.aigoya.store.dto.request.AddEmployeeRequest;
import kr.elroy.aigoya.store.dto.response.EmployeeResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "직원 관리", description = "내 가게의 직원 관리 API (인증 필요)")
@RequestMapping("/v1/stores/me/employees")
public interface EmployeeApi {

    @PostMapping
    EmployeeResponse addEmployee(
            @Parameter(hidden = true) @AuthenticationPrincipal(expression = "id") Long storeId,
            @RequestBody AddEmployeeRequest request
    );

    @GetMapping
    List<EmployeeResponse> getEmployees(
            @Parameter(hidden = true) @AuthenticationPrincipal(expression = "id") Long storeId
    );

    @DeleteMapping("/{employeeId}")
    void removeEmployee(
            @Parameter(hidden = true) @AuthenticationPrincipal(expression = "id") Long storeId,
            @PathVariable Long employeeId
    );
}