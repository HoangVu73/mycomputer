package com.example.my_computer.controller;

import com.example.my_computer.dto.LoginDTO;
import com.example.my_computer.dto.LoginResponse;
import com.example.my_computer.entity.Users;
import com.example.my_computer.repository.UsersRepository;
import com.example.my_computer.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users Management", description = "Operations related to user management")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Ngăn Spring tự động bind trường "profileImage" vào đối tượng Users
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("profileImage");
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả người dùng")
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin người dùng theo id")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        Optional<Users> user = usersRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Tạo mới một người dùng với upload file")
    public Users createUser(@ModelAttribute Users user,
                            @RequestParam(value = "profileImage", required = false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                String uploadsDir = "uploads/";
                File uploadDir = new File(uploadsDir);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String fileName = file.getOriginalFilename();
                Path filePath = Paths.get(uploadsDir, fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                String imageUrl = "http://localhost:8080/uploads/" + fileName;
                user.setProfileImage(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return usersRepository.save(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin người dùng với upload file")
    public ResponseEntity<Users> updateUser(@PathVariable Integer id,
                                            @ModelAttribute Users user,
                                            @RequestParam(value = "profileImage", required = false) MultipartFile file) {
        Optional<Users> optionalUser = usersRepository.findById(id);
        if (optionalUser.isPresent()) {
            Users existingUser = optionalUser.get();

            // Nếu username mới null hoặc rỗng, giữ nguyên giá trị cũ
            String newUsername = user.getUsername();
            existingUser.setUsername((newUsername == null || newUsername.trim().isEmpty())
                    ? existingUser.getUsername()
                    : newUsername);

            // Nếu mật khẩu mới null hoặc rỗng, giữ nguyên mật khẩu cũ
            String newPassword = user.getPassword();
            existingUser.setPassword((newPassword == null || newPassword.trim().isEmpty())
                    ? existingUser.getPassword()
                    : newPassword);

            // Nếu fullName mới null hoặc rỗng, giữ nguyên giá trị cũ
            String newFullName = user.getFullName();
            existingUser.setFullName((newFullName == null || newFullName.trim().isEmpty())
                    ? existingUser.getFullName()
                    : newFullName);

            // Nếu email mới null hoặc rỗng, giữ nguyên giá trị cũ
            String newEmail = user.getEmail();
            existingUser.setEmail((newEmail == null || newEmail.trim().isEmpty())
                    ? existingUser.getEmail()
                    : newEmail);

            // Role
            existingUser.setRole(user.getRole() != null ? user.getRole() : existingUser.getRole());

            // Xử lý file upload nếu có
            if (file != null && !file.isEmpty()) {
                try {
                    String uploadsDir = "uploads/";
                    File uploadDir = new File(uploadsDir);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    String fileName = file.getOriginalFilename();
                    Path filePath = Paths.get(uploadsDir, fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    String imageUrl = "http://localhost:8080/uploads/" + fileName;
                    existingUser.setProfileImage(imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Users updatedUser = usersRepository.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa người dùng")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        Optional<Users> user = usersRepository.findById(id);
        if (user.isPresent()) {
            usersRepository.delete(user.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/register")
    @Operation(summary = "Đăng ký người dùng với thông tin JSON")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        // Kiểm tra xem username hoặc email đã tồn tại chưa
        Optional<Users> existingUser = usersRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tài khoản hoặc email đã tồn tại");
        }
        // Lưu người dùng mới (lưu ý mã hóa password nếu cần)
        Users savedUser = usersRepository.save(user);
        String token = jwtUtil.generateJwtToken(savedUser.getUsername());
        return ResponseEntity.ok(new LoginResponse(savedUser, token));
    }

    // Chỉ có 1 endpoint đăng nhập duy nhất (không được định nghĩa trùng lặp)
    @PostMapping("/login")
    @Operation(summary = "Đăng nhập với tài khoản (username hoặc email) và mật khẩu")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Optional<Users> userOpt = usersRepository.findByUsernameOrEmail(loginDTO.getAccount(), loginDTO.getAccount());
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            if (user.getPassword().equals(loginDTO.getPassword())) {
                String token = jwtUtil.generateJwtToken(user.getUsername());
                return ResponseEntity.ok(new LoginResponse(user, token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai mật khẩu");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản không tồn tại");
        }
    }
}
