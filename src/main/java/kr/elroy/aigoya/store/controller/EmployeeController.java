package kr.elroy.aigoya.store.controller;

import kr.elroy.aigoya.store.api.EmployeeApi;
import kr.elroy.aigoya.store.domain.Employee;
import kr.elroy.aigoya.store.dto.request.EmployeeRequest;
import kr.elroy.aigoya.store.dto.response.EmployeeResponse;
import kr.elroy.aigoya.store.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

    private final EmployeeService employeeService;

    @Override
    public EmployeeResponse addEmployee(
            @AuthenticationPrincipal(expression = "id") Long storeId,
            EmployeeRequest request
    ) {
        Employee employee = employeeService.addEmployee(storeId, request);

        return EmployeeResponse.of(employee);
    }

    @Override
    public List<EmployeeResponse> getEmployees(
            @AuthenticationPrincipal(expression = "id") Long storeId
    ) {
        List<Employee> employees = employeeService.getEmployees(storeId);

        return employees.stream()
                .map(EmployeeResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public void removeEmployee(
            @AuthenticationPrincipal(expression = "id") Long storeId,
            Long employeeId
    ) {
        employeeService.removeEmployee(storeId, employeeId);
    }
}