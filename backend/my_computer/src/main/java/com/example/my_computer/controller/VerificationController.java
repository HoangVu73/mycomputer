package com.example.my_computer.controller;

import com.example.my_computer.dto.EmailRequest;
import com.example.my_computer.entity.Users;
import com.example.my_computer.repository.UsersRepository;
import com.example.my_computer.model.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Verification", description = "Gửi mã xác thực cho đăng ký và quên mật khẩu")
public class VerificationController {

    private static final Logger logger = LoggerFactory.getLogger(VerificationController.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EmailService emailService;

    // Gửi mã xác thực cho đăng ký
    @PostMapping("/send-verification-code-registration")
    @Operation(summary = "Gửi mã xác thực cho đăng ký")
    public ResponseEntity<String> sendVerificationCodeRegistration(@RequestBody EmailRequest request) {
        logger.info("Nhận yêu cầu gửi mã xác thực đến email: {}", request.getEmail());

        Optional<Users> existingUser = usersRepository.findByUsernameOrEmail(request.getEmail(), request.getEmail());
        if (existingUser.isPresent()) {
            logger.warn("Email {} đã tồn tại trong hệ thống!", request.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email đã được sử dụng");
        }

        // Tạo mã xác thực
        String verificationCode = generateVerificationCode();
        logger.info("Mã xác thực được tạo: {}", verificationCode);

        try {
            emailService.sendVerificationCode(request.getEmail(), verificationCode);
            logger.info("Mã xác thực đã được gửi thành công đến email {}", request.getEmail());
            return ResponseEntity.ok("Mã xác thực đã được gửi tới email của bạn");
        } catch (Exception e) {
            logger.error("Lỗi khi gửi email: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể gửi email. Vui lòng thử lại sau.");
        }
    }

    // Hàm sinh mã xác thực ngẫu nhiên (6 chữ số)
    private String generateVerificationCode() {
        int code = 100000 + new Random().nextInt(900000);
        return String.valueOf(code);
    }
}
