package com.example.my_computer.controller;

import com.example.my_computer.entity.Brands;
import com.example.my_computer.repository.BrandsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/brands")
public class BrandsController {

    @Autowired
    private BrandsRepository brandsRepository;

    // Lấy danh sách tất cả thương hiệu
    @GetMapping
    public List<Brands> getAllBrands() {
        return brandsRepository.findAll();
    }

    // Lấy thông tin thương hiệu theo id
    @GetMapping("/{id}")
    public ResponseEntity<Brands> getBrandById(@PathVariable Integer id) {
        Optional<Brands> brand = brandsRepository.findById(id);
        return brand.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Tạo mới thương hiệu (kiểm tra tên không trùng)
    @PostMapping
    public ResponseEntity<?> createBrand(@RequestBody Brands brand) {
        if (brandsRepository.findByName(brand.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Brand with name '" + brand.getName() + "' already exists.");
        }
        Brands savedBrand = brandsRepository.save(brand);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBrand);
    }

    // Cập nhật thương hiệu
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Integer id, @RequestBody Brands brandDetails) {
        Optional<Brands> optionalBrand = brandsRepository.findById(id);
        if (!optionalBrand.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Brands brand = optionalBrand.get();
        // Nếu tên thay đổi, kiểm tra tên mới có tồn tại chưa
        if (!brand.getName().equals(brandDetails.getName()) &&
                brandsRepository.findByName(brandDetails.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Brand with name '" + brandDetails.getName() + "' already exists.");
        }
        brand.setName(brandDetails.getName());
        brand.setDescription(brandDetails.getDescription());
        Brands updatedBrand = brandsRepository.save(brand);
        return ResponseEntity.ok(updatedBrand);
    }

    // Xoá thương hiệu
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Integer id) {
        if (!brandsRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        brandsRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
