package com.example.my_computer.repository;

import com.example.my_computer.dto.OrderItemDTO;
import com.example.my_computer.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    // Thêm phương thức này để tìm OrderItem theo order.id
    List<OrderItem> findByOrder_Id(Integer orderId);

    @Query("SELECT new com.example.my_computer.dto.OrderItemDTO(oi.id, oi.productId, p.name, p.image, oi.quantity, oi.price) " +
            "FROM OrderItem oi JOIN Product p ON oi.productId = p.id " +
            "WHERE oi.order.id = :orderId")
    List<OrderItemDTO> findOrderItemsWithProductInfoByOrderId(@Param("orderId") Integer orderId);
}
