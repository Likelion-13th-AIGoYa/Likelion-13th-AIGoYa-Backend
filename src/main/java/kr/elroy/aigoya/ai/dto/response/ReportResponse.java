package kr.elroy.aigoya.ai.dto.response;

import kr.elroy.aigoya.store.config.CurrentStoreId;

public record ReportResponse(
        @CurrentStoreId
        Long storeId,
        Long chatRoomId,
        String report
) {
}
