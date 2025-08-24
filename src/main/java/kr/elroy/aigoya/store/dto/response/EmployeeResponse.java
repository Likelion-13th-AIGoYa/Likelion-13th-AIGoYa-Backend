package kr.elroy.aigoya.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.elroy.aigoya.store.domain.Employee;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Schema(description = "직원 정보 응답")
public record EmployeeResponse(
        @Schema(description = "직원 ID", example = "101")
        Long id,

        @Schema(description = "직원 이름", example = "김아르바")
        String name,

        @Schema(description = "직책", example = "파트타이머")
        String role,

        @Schema(description = "시급 (원 단위)", example = "10000")
        Integer hourlyWage,

        @Schema(description = "근무 시작 시간 (HH:mm)", example = "09:00")
        String workStartTime,

        @Schema(description = "근무 종료 시간 (HH:mm)", example = "18:00")
        String workEndTime,

        @Schema(description = "근무 요일 목록", example = "[\"MONDAY\", \"TUESDAY\"]")
        Set<DayOfWeek> workDays
) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static EmployeeResponse of(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getRole(),
                employee.getHourlyWage(),
                formatTime(employee.getWorkStartTime()),
                formatTime(employee.getWorkEndTime()),
                employee.getWorkDays()
        );
    }

    private static String formatTime(LocalTime time) {
        if (time == null) {
            return null;
        }
        return time.format(TIME_FORMATTER);
    }
}
