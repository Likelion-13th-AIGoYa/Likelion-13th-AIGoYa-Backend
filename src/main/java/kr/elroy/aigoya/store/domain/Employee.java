package kr.elroy.aigoya.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.elroy.aigoya.store.domain.converter.DayOfWeekSetConverter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

    @Column(nullable = false)
    private Integer hourlyWage;

    @Column
    private LocalTime workStartTime;

    @Column
    private LocalTime workEndTime;

    @Column(name = "work_days", length = 100)
    @Convert(converter = DayOfWeekSetConverter.class)
    private Set<DayOfWeek> workDays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public void updateInfo(String name, String role, Integer hourlyWage, LocalTime workStartTime, LocalTime workEndTime, Set<DayOfWeek> workDays) {
        this.name = name;
        this.role = role;
        this.hourlyWage = hourlyWage;
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
        this.workDays = workDays;
    }
}