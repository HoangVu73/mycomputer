package com.example.my_computer.controller;

import com.example.my_computer.dto.ProductDTO;
import com.example.my_computer.entity.Product;
import com.example.my_computer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // Lấy danh sách sản phẩm kèm theo điểm trung bình (nếu cần)
    @GetMapping("/with-average-rating")
    public List<ProductDTO> getProductsWithAverageRating() {
        return productRepository.findAllWithAverageRating();
    }

    // Lấy danh sách tất cả sản phẩm
    @GetMapping
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    // Lấy chi tiết sản phẩm theo id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Thêm sản phẩm mới
    @PostMapping
    public Product createProduct(@RequestBody Product product){
        return productRepository.save(product);
    }

    // Cập nhật thông tin sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product productDetails){
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Product product = productOptional.get();
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setSaleStatus(productDetails.getSaleStatus());
        product.setImage(productDetails.getImage());
        product.setDiscount(productDetails.getDiscount());
        product.setBrandId(productDetails.getBrandId());
        // Cập nhật các trường mới nếu có
        product.setCpu(productDetails.getCpu());
        product.setRam(productDetails.getRam());
        product.setHardDrive(productDetails.getHardDrive());

        Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    // Xoá sản phẩm theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(productOptional.get());
        return ResponseEntity.noContent().build();
    }
}
