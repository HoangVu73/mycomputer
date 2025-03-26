package com.example.my_computer.controller;

import com.example.my_computer.dto.OrderItemDTO;
import com.example.my_computer.entity.OrderItem;
import com.example.my_computer.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/order_items")
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Lấy danh sách tất cả order items
    @GetMapping
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    // Lấy chi tiết order item theo id
    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Integer id) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
        return orderItemOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Lấy danh sách order items theo order id (dạng entity)
    @GetMapping("/order/{orderId}")
    public List<OrderItem> getOrderItemsByOrderId(@PathVariable Integer orderId) {
        return orderItemRepository.findByOrder_Id(orderId);
    }

    // Endpoint mới trả về OrderItemDTO với thông tin sản phẩm (name, image, …)
    @GetMapping("/dto/order/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getOrderItemsDTOByOrderId(@PathVariable Integer orderId) {
        List<OrderItemDTO> dtos = orderItemRepository.findOrderItemsWithProductInfoByOrderId(orderId);
        if(dtos.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dtos);
    }

    // Tạo mới order item
    @PostMapping
    public OrderItem createOrderItem(@RequestBody OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    // Cập nhật order item
    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable Integer id, @RequestBody OrderItem orderItemDetails) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
        if (!orderItemOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        OrderItem orderItem = orderItemOptional.get();
        orderItem.setQuantity(orderItemDetails.getQuantity());
        orderItem.setPrice(orderItemDetails.getPrice());
        orderItem.setProductId(orderItemDetails.getProductId());
        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return ResponseEntity.ok(updatedOrderItem);
    }

    // Xoá order item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Integer id) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
        if (!orderItemOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        orderItemRepository.delete(orderItemOptional.get());
        return ResponseEntity.noContent().build();
    }
}
