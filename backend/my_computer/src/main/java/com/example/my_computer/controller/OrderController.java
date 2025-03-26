package com.example.my_computer.controller;

import com.example.my_computer.dto.OrderReportDTO;
import com.example.my_computer.entity.Order;
import com.example.my_computer.entity.OrderStatus;
import com.example.my_computer.entity.ReviewStatus;
import com.example.my_computer.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

// Các import cho xuất PDF với iText 7
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // Lấy danh sách đơn hàng, nếu có truyền userId thì lọc theo userId, ngược lại trả về tất cả
    @GetMapping
    public List<Order> getAllOrders(@RequestParam(value = "userId", required = false) Integer userId) {
        if (userId != null) {
            return orderRepository.findByUserId(userId);
        }
        return orderRepository.findAll();
    }

    // Lấy chi tiết đơn hàng theo id
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Tạo mới một đơn hàng
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> item.setOrder(order));
        }
        return orderRepository.save(order);
    }

    // Cập nhật đơn hàng (full update)
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody Order orderDetails) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Order order = orderOptional.get();
        order.setUserId(orderDetails.getUserId());
        order.setTotalAmount(orderDetails.getTotalAmount());
        order.setStatus(orderDetails.getStatus());
        order.setRecipientName(orderDetails.getRecipientName());
        order.setPhone(orderDetails.getPhone());
        order.setAddress(orderDetails.getAddress());
        order.setReviewStatus(orderDetails.getReviewStatus());
        Order updatedOrder = orderRepository.save(order);
        return ResponseEntity.ok(updatedOrder);
    }

    // Huỷ đơn hàng (chỉ cập nhật trạng thái)
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Integer id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Order order = orderOptional.get();
        order.setStatus(OrderStatus.CANCELLED);
        Order updatedOrder = orderRepository.save(order);
        return ResponseEntity.ok(updatedOrder);
    }

    // Endpoint cập nhật trạng thái đánh giá thành công: reviewStatus chuyển thành REVIEWED
    @PutMapping("/{id}/review")
    public ResponseEntity<Order> reviewOrder(@PathVariable Integer id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Order order = orderOptional.get();
        order.setReviewStatus(ReviewStatus.REVIEWED);
        Order updatedOrder = orderRepository.save(order);
        return ResponseEntity.ok(updatedOrder);
    }

    // Xoá đơn hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        orderRepository.delete(orderOptional.get());
        return ResponseEntity.noContent().build();
    }

    // --- Các endpoint báo cáo thống kê ---

    // Báo cáo theo ngày
    @GetMapping("/report/daily")
    public ResponseEntity<List<OrderReportDTO>> getDailyReport() {
        List<OrderReportDTO> report = orderRepository.getDailyReport();
        return ResponseEntity.ok(report);
    }

    // Báo cáo theo tuần
    @GetMapping("/report/weekly")
    public ResponseEntity<List<OrderReportDTO>> getWeeklyReport() {
        List<OrderReportDTO> report = orderRepository.getWeeklyReport();
        return ResponseEntity.ok(report);
    }

    // Báo cáo theo tháng
    @GetMapping("/report/monthly")
    public ResponseEntity<List<OrderReportDTO>> getMonthlyReport() {
        List<OrderReportDTO> report = orderRepository.getMonthlyReport();
        return ResponseEntity.ok(report);
    }

    // Báo cáo theo năm
    @GetMapping("/report/yearly")
    public ResponseEntity<List<OrderReportDTO>> getYearlyReport() {
        List<OrderReportDTO> report = orderRepository.getYearlyReport();
        return ResponseEntity.ok(report);
    }

    // Báo cáo tùy chỉnh (custom): yêu cầu các tham số start và end (dạng ISO String)
    @GetMapping("/report/custom")
    public ResponseEntity<List<OrderReportDTO>> getCustomReport(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        List<OrderReportDTO> report = orderRepository.getCustomReport(start, end);
        return ResponseEntity.ok(report);
    }

    // --- Endpoint lấy số đơn hàng mới trong 24h qua ---
    @GetMapping("/new-orders")
    public ResponseEntity<Integer> getNewOrdersCount() {
        // Bạn cần cài đặt phương thức countNewOrdersInLast24Hours() trong OrderRepository
        int count = orderRepository.countNewOrdersInLast24Hours();
        return ResponseEntity.ok(count);
    }

    // --- Endpoint xuất báo cáo CSV ---
    @GetMapping("/export/csv")
    public void exportCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"bao_cao.csv\"");

        // Ví dụ: xuất báo cáo theo ngày (bạn có thể chọn loại khác)
        List<OrderReportDTO> report = orderRepository.getDailyReport();
        PrintWriter writer = response.getWriter();

        // Ghi tiêu đề CSV
        writer.println("Period,OrderCount,TotalRevenue");

        // Ghi dữ liệu báo cáo
        for (OrderReportDTO item : report) {
            writer.println(item.getPeriod() + "," + item.getOrderCount() + "," + item.getTotalRevenue());
        }

        writer.flush();
        writer.close();
    }

    // --- Endpoint xuất báo cáo PDF ---
    @GetMapping("/export/pdf")
    public void exportPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"bao_cao.pdf\"");

        // Sử dụng iText 7 để tạo file PDF
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Thêm tiêu đề cho báo cáo
        document.add(new Paragraph("Báo cáo doanh thu - Daily Report").setBold().setFontSize(16));
        document.add(new Paragraph(" "));

        // Lấy dữ liệu báo cáo
        List<OrderReportDTO> report = orderRepository.getDailyReport();
        for (OrderReportDTO item : report) {
            String line = "Period: " + item.getPeriod() +
                    ", Order Count: " + item.getOrderCount() +
                    ", Total Revenue: " + item.getTotalRevenue();
            document.add(new Paragraph(line));
        }

        document.close();
    }
}
