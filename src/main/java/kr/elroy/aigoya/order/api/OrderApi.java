package kr.elroy.aigoya.order.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.elroy.aigoya.order.dto.request.CreateOrderRequest;
import kr.elroy.aigoya.order.dto.response.OrderResponse;
import kr.elroy.aigoya.order.dto.request.UpdateOrderRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "주문 API", description = "주문 API")
@RestController
@Validated
@RequestMapping("/v1/orders")
public interface OrderApi {

    @Operation(summary = "주문 조회", description = "주문 정보를 조회합니다.")
    @GetMapping("/{id}")
    OrderResponse getOrder(
            @Parameter(description = "주문 ID", required = true, example = "1")
            @PathVariable
            Long id
    );

    @Operation(summary = "전체 주문 조회", description = "모든 주문을 조회합니다.")
    @GetMapping
    List<OrderResponse> getAllOrders();

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @PostMapping
    OrderResponse createOrder(
            @Valid
            @RequestBody
            @Parameter(description = "주문 정보", required = true)
            CreateOrderRequest request
    );

    @Operation(summary = "주문 수정", description = "기존 주문을 수정합니다.")
    @PutMapping("/{id}")
    OrderResponse updateOrder(
            @Parameter(description = "주문 ID", required = true, example = "1")
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            @Parameter(description = "수정할 주문 정보", required = true)
            UpdateOrderRequest request
    );

    @Operation(summary = "주문 삭제", description = "주문을 삭제합니다.")
    @DeleteMapping("/{id}")
    void deleteOrder(
            @Parameter(description = "주문 ID", required = true, example = "1")
            @PathVariable
            Long id
    );
}
