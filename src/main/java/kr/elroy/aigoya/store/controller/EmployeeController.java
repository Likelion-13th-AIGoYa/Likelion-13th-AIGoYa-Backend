package kr.elroy.aigoya.store.controller;

import kr.elroy.aigoya.store.api.EmployeeApi;
import kr.elroy.aigoya.store.domain.Employee;
import kr.elroy.aigoya.store.dto.request.AddEmployeeRequest;
import kr.elroy.aigoya.store.dto.response.EmployeeResponse;
import kr.elroy.aigoya.store.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

    private final EmployeeService employeeService;

    @Override
    public EmployeeResponse addEmployee(
            Long storeId,
            AddEmployeeRequest request
    ) {
        Employee employee = employeeService.addEmployee(storeId, request);
        return EmployeeResponse.of(employee);
    }

    @Override
    public List<EmployeeResponse> getEmployees(
            Long storeId
    ) {
        List<Employee> employees = employeeService.getEmployees(storeId);
        return employees.stream()
                .map(EmployeeResponse::of)
                .toList();
    }

    @Override
    public void removeEmployee(
            Long storeId,
            Long employeeId
    ) {
        employeeService.removeEmployee(storeId, employeeId);
    }
}