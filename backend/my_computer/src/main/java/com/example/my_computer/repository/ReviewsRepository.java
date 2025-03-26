package com.example.my_computer.repository;

import com.example.my_computer.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {
    List<Reviews> findByOrderIdAndUserId(Integer orderId, Integer userId);
    List<Reviews> findByProductId(Integer productId);
}
