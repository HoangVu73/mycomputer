package com.example.my_computer.controller;

import com.example.my_computer.entity.Reviews;
import com.example.my_computer.repository.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewsController {

    @Autowired
    private ReviewsRepository reviewsRepository;

    // Lấy danh sách tất cả các review
    @GetMapping
    public List<Reviews> getAllReviews() {
        return reviewsRepository.findAll();
    }

    // Lấy chi tiết 1 review theo id
    @GetMapping("/{id}")
    public ResponseEntity<Reviews> getReviewById(@PathVariable("id") Integer id) {
        Optional<Reviews> review = reviewsRepository.findById(id);
        return review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Lấy danh sách review theo product id
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Reviews>> getReviewsByProductId(@PathVariable("productId") Integer productId) {
        List<Reviews> reviews = reviewsRepository.findByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    // Tạo mới 1 review (một đơn hàng chỉ review một lần)
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Reviews review) {
        // Kiểm tra nếu người dùng đã review đơn hàng này rồi
        List<Reviews> existingReviews = reviewsRepository.findByOrderIdAndUserId(review.getOrderId(), review.getUserId());
        if (!existingReviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Đơn hàng này đã được đánh giá bởi người dùng.");
        }
        Reviews savedReview = reviewsRepository.save(review);
        return ResponseEntity.ok(savedReview);
    }

    // Cập nhật review (không bao gồm xóa)
    @PutMapping("/{id}")
    public ResponseEntity<Reviews> updateReview(@PathVariable("id") Integer id, @RequestBody Reviews reviewDetails) {
        Optional<Reviews> optionalReview = reviewsRepository.findById(id);
        if (!optionalReview.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Reviews review = optionalReview.get();
        review.setProductId(reviewDetails.getProductId());
        review.setUserId(reviewDetails.getUserId());
        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());
        Reviews updatedReview = reviewsRepository.save(review);
        return ResponseEntity.ok(updatedReview);
    }
}
