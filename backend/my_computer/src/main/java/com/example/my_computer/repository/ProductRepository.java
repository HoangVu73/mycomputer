package com.example.my_computer.repository;

import com.example.my_computer.dto.ProductDTO;
import com.example.my_computer.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Cập nhật truy vấn để trả về các trường mới (cpu, ram, hardDrive)
    @Query("SELECT new com.example.my_computer.dto.ProductDTO(" +
            "p.id, p.name, p.description, p.price, p.quantity, " +
            "p.saleStatus, p.image, p.discount, " +
            "p.cpu, p.ram, p.hardDrive, COALESCE(AVG(r.rating), 0.0)) " +
            "FROM Product p LEFT JOIN Reviews r ON p.id = r.productId GROUP BY p.id")
    List<ProductDTO> findAllWithAverageRating();
}
