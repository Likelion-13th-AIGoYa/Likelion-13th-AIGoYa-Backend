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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final StoreRepository storeRepository;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Employee createEmployee(Long storeId, AddEmployeeRequest request) {
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

        Employee employee = Employee.builder()
                .name(request.name())
                .role(request.role())
                .hourlyWage(request.hourlyWage())
                .store(store)
                .workStartTime(parseTime(request.workStartTime()))
                .workEndTime(parseTime(request.workEndTime()))
                .workDays(request.workDays())
                .build();

        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> getEmployees(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new StoreNotFoundException();
        }
        return employeeRepository.findByStoreId(storeId);
    }

    public void updateEmployee(Long storeId, Long employeeId, AddEmployeeRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);

        if (!Objects.equals(employee.getStore().getId(), storeId)) {
            throw new AccessDeniedException();
        }

        employee.updateInfo(
                request.name(),
                request.role(),
                request.hourlyWage(),
                parseTime(request.workStartTime()),
                parseTime(request.workEndTime()),
                request.workDays()
        );
    }

    public void removeEmployee(Long storeId, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);

        if (!Objects.equals(employee.getStore().getId(), storeId)) {
            throw new AccessDeniedException();
        }

        employeeRepository.delete(employee);
    }

    private LocalTime parseTime(String timeString) {
        if (timeString == null || timeString.isBlank()) {
            return null;
        }
        return LocalTime.parse(timeString, TIME_FORMATTER);
    }
}
