package kr.elroy.aigoya.store.controller;

import kr.elroy.aigoya.store.api.EmployeeApi;
import kr.elroy.aigoya.store.domain.Employee;
import kr.elroy.aigoya.store.dto.request.AddEmployeeRequest;
import kr.elroy.aigoya.store.dto.response.EmployeeResponse;
import kr.elroy.aigoya.store.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

    private final EmployeeService employeeService;

    @Override
    public EmployeeResponse createEmployee(Long storeId, AddEmployeeRequest request) {
        Employee employee = employeeService.createEmployee(storeId, request);
        return EmployeeResponse.of(employee);
    }

    @Override
    public List<EmployeeResponse> getEmployees(Long storeId) {
        List<Employee> employees = employeeService.getEmployees(storeId);
        return employees.stream()
                .map(EmployeeResponse::of)
                .toList();
    }

    @Override
    public void updateEmployee(Long storeId, Long employeeId, AddEmployeeRequest request) {
        employeeService.updateEmployee(storeId, employeeId, request);
    }

    @Override
    public void removeEmployee(Long storeId, Long employeeId) {
        employeeService.removeEmployee(storeId, employeeId);
    }
}
