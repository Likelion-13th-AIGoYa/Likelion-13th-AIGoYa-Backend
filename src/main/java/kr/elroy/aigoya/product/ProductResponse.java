package kr.elroy.aigoya.product;

public record ProductResponse(
        Long productId,
        String productName,
        Long price
) {
}
