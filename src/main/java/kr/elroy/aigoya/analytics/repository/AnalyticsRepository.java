package kr.elroy.aigoya.analytics.repository;

import kr.elroy.aigoya.analytics.dto.internal.DailySummaryRawDto;
import kr.elroy.aigoya.analytics.dto.response.HourlySalesResponse;
import kr.elroy.aigoya.analytics.dto.response.MenuAnalysisResponse;
import kr.elroy.aigoya.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsRepository extends JpaRepository<Order, Long> {

    @Query("SELECT new kr.elroy.aigoya.analytics.dto.internal.DailySummaryRawDto(SUM(o.totalPrice), COUNT(o.id)) " +
           "FROM orders o " +
           "WHERE o.store.id = :storeId AND o.orderedAt >= :startOfDay AND o.orderedAt < :endOfDay")
    DailySummaryRawDto findDailySummaryRaw(
            @Param("storeId") Long storeId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("SELECT new kr.elroy.aigoya.analytics.dto.response.HourlySalesResponse(hour(o.orderedAt), SUM(o.totalPrice)) " +
           "FROM orders o " +
           "WHERE o.store.id = :storeId AND o.orderedAt >= :startOfDay AND o.orderedAt < :endOfDay " +
           "GROUP BY hour(o.orderedAt) " +
           "ORDER BY hour(o.orderedAt) ASC")
    List<HourlySalesResponse> findHourlySales(
            @Param("storeId") Long storeId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query(value = "SELECT new kr.elroy.aigoya.analytics.dto.response.MenuAnalysisResponse(p.name, SUM(op.quantity), SUM(op.quantity * p.price)) " +
            "FROM order_product op JOIN op.product p JOIN op.order o " +
            "WHERE o.store.id = :storeId AND o.orderedAt >= :startDate AND o.orderedAt < :endDate " +
            "GROUP BY p.id, p.name " +
            "ORDER BY SUM(op.quantity) DESC, SUM(op.quantity * p.price) DESC")
    Page<MenuAnalysisResponse> findMenuAnalysisTop(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query(value = "SELECT new kr.elroy.aigoya.analytics.dto.response.MenuAnalysisResponse(p.name, SUM(op.quantity), SUM(op.quantity * p.price)) " +
            "FROM order_product op JOIN op.product p JOIN op.order o " +
            "WHERE o.store.id = :storeId AND o.orderedAt >= :startDate AND o.orderedAt < :endDate " +
            "GROUP BY p.id, p.name " +
            "ORDER BY SUM(op.quantity) ASC, SUM(op.quantity * p.price) ASC")
    Page<MenuAnalysisResponse> findMenuAnalysisBottom(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
