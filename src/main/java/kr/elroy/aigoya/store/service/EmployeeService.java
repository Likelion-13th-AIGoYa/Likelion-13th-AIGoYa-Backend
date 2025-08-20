package kr.elroy.aigoya.store.service;

import kr.elroy.aigoya.exception.AccessDeniedException;
import kr.elroy.aigoya.store.domain.Employee;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.dto.request.AddEmployeeRequest;
import kr.elroy.aigoya.store.exception.EmployeeNotFoundException;
import kr.elroy.aigoya.store.exception.StoreNotFoundException;
import kr.elroy.aigoya.store.repository.EmployeeRepository;
import kr.elroy.aigoya.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final StoreRepository storeRepository;

    public Employee addEmployee(Long storeId, AddEmployeeRequest request) {
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

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
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);
        
        return employeeRepository.findByStore(store);
    }

    public void removeEmployee(Long storeId, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);

        if (!employee.getStore().getId().equals(storeId)) {
            throw new AccessDeniedException();
        }

        employeeRepository.delete(employee);
    }
}