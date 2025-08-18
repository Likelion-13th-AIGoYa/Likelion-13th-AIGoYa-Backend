package kr.elroy.aigoya.store.repository;

import kr.elroy.aigoya.store.domain.Employee;
import kr.elroy.aigoya.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByStore(Store store);
}