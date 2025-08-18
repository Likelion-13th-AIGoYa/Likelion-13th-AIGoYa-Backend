package kr.elroy.aigoya.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.elroy.aigoya.store.domain.Employee;
import java.time.LocalDateTime;

@Schema(description = "직원 정보 응답")
public record EmployeeResponse(
        @Schema(description = "직원 ID", example = "101")
        Long id,

        @Schema(description = "직원 이름", example = "김아르바")
        String name,

        @Schema(description = "직책", example = "파트타이머")
        String role,

        @Schema(description = "마지막 출근 시간", example = "2025-08-17T09:00:00")
        LocalDateTime checkInTime,

        @Schema(description = "시급 (원 단위)", example = "10000")
        Integer hourlyWage
) {

    public static EmployeeResponse of(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getRole(),
                employee.getCheckInTime(),
                employee.getHourlyWage()
        );
    }
}