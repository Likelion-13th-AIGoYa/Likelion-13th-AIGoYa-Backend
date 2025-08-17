package kr.elroy.aigoya.store.repository;

import kr.elroy.aigoya.store.domain.Employee;
import kr.elroy.aigoya.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    /**
     * 특정 가게(Store)에 소속된 모든 직원 조회
     * @param store 조회할 가게 엔티티
     * @return 해당 가게의 직원 목록
     */
    List<Employee> findByStore(Store store);
}