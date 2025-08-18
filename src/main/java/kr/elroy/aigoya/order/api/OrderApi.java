package kr.elroy.aigoya.order.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.elroy.aigoya.order.dto.request.CreateOrderRequest;
import kr.elroy.aigoya.order.dto.response.OrderResponse;
import kr.elroy.aigoya.order.dto.request.UpdateOrderRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "주문 API (관리자용)", description = "모든 가게 주문을 관리하는 API")
@RestController
@Validated
@RequestMapping("/v1/orders")
public interface OrderApi {

    @Operation(summary = "주문 조회", description = "주문 ID와 가게 ID를 함께 조회합니다.")
    @GetMapping("/{id}")
    OrderResponse getOrder(
            @Parameter(description = "주문 ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "가게 ID", required = true, example = "10")
            @RequestParam Long storeId
    );

    @Operation(summary = "전체 주문 조회", description = "모든 가게 주문을 조회합니다.")
    @GetMapping
    List<OrderResponse> getAllOrders(
            @Parameter(description = "가게 ID (선택)", example = "10")
            @RequestParam(required = false) Long storeId
    );

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @PostMapping
    OrderResponse createOrder(
            @Valid
            @RequestBody
            @Parameter(description = "주문 정보", required = true)
            CreateOrderRequest request,
            @Parameter(description = "가게 ID", required = true, example = "10")
            @RequestParam Long storeId
    );

    @Operation(summary = "주문 수정", description = "주문 ID와 가게 ID를 기반으로 주문을 수정합니다.")
    @PutMapping("/{id}")
    OrderResponse updateOrder(
            @Parameter(description = "주문 ID", required = true, example = "1")
            @PathVariable Long id,
            @Valid
            @RequestBody
            @Parameter(description = "수정할 주문 정보", required = true)
            UpdateOrderRequest request,
            @Parameter(description = "가게 ID", required = true, example = "10")
            @RequestParam Long storeId
    );

    @Operation(summary = "주문 삭제", description = "주문 ID와 가게 ID를 기반으로 주문을 삭제합니다.")
    @DeleteMapping("/{id}")
    void deleteOrder(
            @Parameter(description = "주문 ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "가게 ID", required = true, example = "10")
            @RequestParam Long storeId
    );
}
