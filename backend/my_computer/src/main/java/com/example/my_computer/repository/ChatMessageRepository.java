package com.example.my_computer.repository;

import java.util.List;
import com.example.my_computer.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    // Bạn có thể thêm các phương thức truy vấn nếu cần, ví dụ tìm theo sender
    List<ChatMessageEntity> findAllBySenderOrTarget(String sender, String target);

}
