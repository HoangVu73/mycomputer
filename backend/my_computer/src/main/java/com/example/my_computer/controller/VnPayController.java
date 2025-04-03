package com.example.my_computer.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class VnPayController {

    @GetMapping("/create_payment")
    public void createPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Thông tin cấu hình VNPay
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TmnCode = "7G8HYVTK";
        String vnp_HashSecret = "UATRNB5AWO8J62HHNGA741VIERBA4UIB";
        String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String vnp_ReturnUrl = "http://localhost:8080/api/payment/vnpay_return"; // URL trả về sau khi thanh toán

        // Tạo các tham số giao dịch
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);

        // Sinh mã giao dịch (transaction reference)
        String txnRef = String.valueOf(System.currentTimeMillis());
        vnp_Params.put("vnp_TxnRef", txnRef);

        // Thông tin đơn hàng, ví dụ:
        vnp_Params.put("vnp_OrderInfo", "Thanh toán đơn hàng: " + txnRef);
        // Số tiền phải được nhân 100 (VNPay yêu cầu số tiền tính theo đơn vị nhỏ nhất của tiền tệ)
        vnp_Params.put("vnp_Amount", "1000000"); // VD: 10,000 VND * 100 = 1,000,000

        // Các tham số khác
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        // Bạn có thể thêm các tham số khác như vnp_BankCode nếu cần

        // Sắp xếp các tham số theo thứ tự bảng chữ cái của key
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String value = vnp_Params.get(fieldName);
            if (value != null && value.length() > 0) {
                hashData.append(fieldName).append("=").append(value);
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
                if (i < fieldNames.size() - 1) {
                    hashData.append("&");
                    query.append("&");
                }
            }
        }

        // Tạo checksum (HMAC SHA512)
        String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        // Tạo URL thanh toán VNPay
        String paymentUrl = vnp_Url + "?" + query.toString();

        // Chuyển hướng khách hàng đến VNPay để tiến hành thanh toán
        response.sendRedirect(paymentUrl);
    }

    // Hàm tạo HMAC SHA512
    private String hmacSHA512(String key, String data) {
        try {
            Mac hmacSha512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmacSha512.init(secretKey);
            byte[] bytes = hmacSha512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo HMAC SHA512", e);
        }
    }

    // Endpoint nhận kết quả trả về từ VNPay sau khi thanh toán
    @GetMapping("/vnpay_return")
    public String vnpayReturn(HttpServletRequest request) {
        // Lấy các tham số trả về từ VNPay
        Map<String, String[]> parameterMap = request.getParameterMap();
        // Tại đây, bạn cần kiểm tra checksum, đối chiếu các tham số (ví dụ: vnp_ResponseCode)
        // và cập nhật trạng thái đơn hàng trong cơ sở dữ liệu.
        // Sau đó thông báo kết quả cho khách hàng.
        return "Thanh toán thành công! Vui lòng kiểm tra email để biết thêm chi tiết.";
    }
}
