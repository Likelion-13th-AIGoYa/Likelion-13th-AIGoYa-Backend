package kr.elroy.aigoya.order.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.elroy.aigoya.order.dto.request.CreateOrderRequest;
import kr.elroy.aigoya.order.dto.request.UpdateOrderRequest;
import kr.elroy.aigoya.order.dto.response.OrderResponse;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "나의 주문 API", description = "나의 주문 API")
@Validated
@RequestMapping("/v1/stores/me/orders")
public interface MyOrderApi {
    @Operation(summary = "나의 주문 목록 조회", description = "현재 토큰 주인의 가게 주문 목록을 조회합니다.")
    @GetMapping
    List<OrderResponse> getMyOrders(
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "나의 주문 조회", description = "현재 토큰 주인의 특정 주문 정보를 조회합니다.")
    @GetMapping("/{id}")
    OrderResponse getMyOrder(
            @Parameter(description = "주문 ID", required = true, example = "1")
            @PathVariable
            Long id,
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "나의 주문 생성", description = "현재 토큰 주인의 가게에 새로운 주문을 생성합니다.")
    @PostMapping
    OrderResponse createMyOrder(
            @Valid
            @RequestBody
            CreateOrderRequest request,
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "나의 주문 수정", description = "현재 토큰 주인의 주문 정보를 수정합니다.")
    @PutMapping("/{id}")
    OrderResponse updateMyOrder(
            @Parameter(description = "주문 ID", required = true, example = "1")
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            UpdateOrderRequest request,
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "나의 주문 삭제", description = "현재 토큰 주인의 주문을 삭제합니다.")
    @DeleteMapping("/{id}")
    void deleteMyOrder(
            @Parameter(description = "주문 ID", required = true, example = "1")
            @PathVariable
            Long id,
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );
}
