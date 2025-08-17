package kr.elroy.aigoya.store.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column
    private LocalDateTime checkInTime;

    @Column(nullable = false)
    private Integer hourlyWage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Builder
    public Employee(String name, String role, Integer hourlyWage, Store store) {
        this.name = name;
        this.role = role;
        this.hourlyWage = hourlyWage;
        this.store = store;
    }

    /**
     * 직원의 출근 시간 기록
     */
    public void checkIn(LocalDateTime time) {
        this.checkInTime = time;
    }

    /**
     * 직원의 정보 수정
     */
    public void updateInfo(String name, String role, Integer hourlyWage) {
        this.name = name;
        this.role = role;
        this.hourlyWage = hourlyWage;
    }
}