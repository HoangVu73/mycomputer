package com.example.my_computer.repository;

import com.example.my_computer.entity.Brands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BrandsRepository extends JpaRepository<Brands, Integer> {
    Optional<Brands> findByName(String name);
}
