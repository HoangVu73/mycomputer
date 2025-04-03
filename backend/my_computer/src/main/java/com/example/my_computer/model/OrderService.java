package com.example.my_computer.model;

import com.example.my_computer.entity.Order;
import com.example.my_computer.entity.OrderItem;
import com.example.my_computer.entity.Product;
import com.example.my_computer.repository.OrderRepository;
import com.example.my_computer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(Order order) {
        // Với mỗi item trong đơn hàng, cập nhật số lượng tồn kho của sản phẩm
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + item.getProductId()));

                // Nếu sản phẩm đã hết hàng (quantity = 0)
                if (product.getQuantity() == 0) {
                    throw new RuntimeException("Sản phẩm " + product.getName() + " đã hết hàng.");
                }

                // Kiểm tra tồn kho: nếu số lượng đặt lớn hơn số lượng hiện có
                if (product.getQuantity() < item.getQuantity()) {
                    throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ hàng (tồn kho hiện tại: " + product.getQuantity() + ")");
                }

                // Giảm số lượng tồn kho
                product.setQuantity(product.getQuantity() - item.getQuantity());
                productRepository.save(product);

                // Liên kết đơn hàng cho từng item
                item.setOrder(order);
            }
        }
        return orderRepository.save(order);
    }
}
