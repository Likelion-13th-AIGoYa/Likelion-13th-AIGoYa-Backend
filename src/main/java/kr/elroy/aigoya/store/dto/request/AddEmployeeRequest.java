package kr.elroy.aigoya.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.DayOfWeek;
import java.util.Set;

@Schema(description = "직원 정보 요청 (추가/수정)")
public record AddEmployeeRequest(
        @NotBlank(message = "직원 이름은 필수입니다.")
        @Schema(description = "직원 이름", example = "김아르바")
        String name,

        @NotBlank(message = "직책은 필수입니다.")
        @Schema(description = "직책 (예: 매니저, 파트타이머)", example = "파트타이머")
        String role,

        @NotNull(message = "시급은 필수입니다.")
        @Positive(message = "시급은 0보다 커야 합니다.")
        @Schema(description = "시급 (원 단위)", example = "10000")
        Integer hourlyWage,

        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "근무 시작 시간은 HH:mm 형식이어야 합니다.")
        @Schema(description = "근무 시작 시간 (HH:mm)", example = "09:00")
        String workStartTime,

        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "근무 종료 시간은 HH:mm 형식이어야 합니다.")
        @Schema(description = "근무 종료 시간 (HH:mm)", example = "18:00")
        String workEndTime,

        @Schema(description = "근무 요일 목록", example = "[\"MONDAY\", \"TUESDAY\", \"WEDNESDAY\"]")
        Set<DayOfWeek> workDays
) {
}