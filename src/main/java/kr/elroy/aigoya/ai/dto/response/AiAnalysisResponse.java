package kr.elroy.aigoya.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AiAnalysisResponse {
    private String advice;
    private long todayTotalSales;
    private boolean isTargetAchieved;
}