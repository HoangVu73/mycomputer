package com.example.my_computer.repository;

import com.example.my_computer.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsernameOrEmail(String username, String email);
}
