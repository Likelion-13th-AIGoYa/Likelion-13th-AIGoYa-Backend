package kr.elroy.aigoya.store.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import kr.elroy.aigoya.store.dto.request.AddEmployeeRequest;
import kr.elroy.aigoya.store.dto.response.EmployeeResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "직원 관리", description = "내 가게의 직원 관리 API (인증 필요)")
@RequestMapping("/v1/stores/me/employees")
public interface EmployeeApi {

    @PostMapping
    EmployeeResponse addEmployee(
            @Parameter(hidden = true)
            @CurrentStoreId
            Long storeId,
            @RequestBody AddEmployeeRequest request
    );

    @GetMapping
    List<EmployeeResponse> getEmployees(
            @Parameter(hidden = true)
            @CurrentStoreId
            Long storeId
    );

    @DeleteMapping("/{employeeId}")
    void removeEmployee(
            @Parameter(hidden = true)
            @CurrentStoreId
            Long storeId,
            @PathVariable Long employeeId
    );
}