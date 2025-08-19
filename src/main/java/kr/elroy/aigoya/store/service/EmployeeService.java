package kr.elroy.aigoya.store.service;

import kr.elroy.aigoya.store.domain.Employee;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.dto.request.AddEmployeeRequest;
import kr.elroy.aigoya.store.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final StoreService storeService;

    public Employee addEmployee(Long storeId, AddEmployeeRequest request) {
        Store store = storeService.getStore(storeId);

        Employee employee = Employee.builder()
                .name(request.name())
                .role(request.role())
                .hourlyWage(request.hourlyWage())
                .store(store)
                .build();

        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> getEmployees(Long storeId) {
        Store store = storeService.getStore(storeId);
        return employeeRepository.findByStore(store);
    }

    public void removeEmployee(Long storeId, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원 ID입니다."));

        if (!employee.getStore().getId().equals(storeId)) {
            throw new IllegalArgumentException("해당 가게의 직원이 아닙니다.");
        }

        employeeRepository.delete(employee);
    }
}