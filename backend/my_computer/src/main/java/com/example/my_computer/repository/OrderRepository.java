package com.example.my_computer.repository;

import com.example.my_computer.dto.OrderReportDTO;
import com.example.my_computer.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Integer userId);

    @Query(value = "SELECT DATE(order_date) as period, COUNT(*) as orderCount, SUM(total_amount) as totalRevenue " +
            "FROM orders " +
            "GROUP BY DATE(order_date)", nativeQuery = true)
    List<OrderReportDTO> getDailyReport();

    @Query(value = "SELECT DATE_FORMAT(order_date, '%Y-%u') as period, COUNT(*) as orderCount, SUM(total_amount) as totalRevenue " +
            "FROM orders " +
            "GROUP BY DATE_FORMAT(order_date, '%Y-%u')", nativeQuery = true)
    List<OrderReportDTO> getWeeklyReport();

    @Query(value = "SELECT DATE_FORMAT(order_date, '%Y-%m') as period, COUNT(*) as orderCount, SUM(total_amount) as totalRevenue " +
            "FROM orders " +
            "GROUP BY DATE_FORMAT(order_date, '%Y-%m')", nativeQuery = true)
    List<OrderReportDTO> getMonthlyReport();

    @Query(value = "SELECT YEAR(order_date) as period, COUNT(*) as orderCount, SUM(total_amount) as totalRevenue " +
            "FROM orders " +
            "GROUP BY YEAR(order_date)", nativeQuery = true)
    List<OrderReportDTO> getYearlyReport();

    // Endpoint báo cáo tùy chỉnh: lấy dữ liệu theo khoảng thời gian (start đến end)
    @Query(value = "SELECT DATE(order_date) as period, COUNT(*) as orderCount, SUM(total_amount) as totalRevenue " +
            "FROM orders " +
            "WHERE order_date BETWEEN :start AND :end " +
            "GROUP BY DATE(order_date)", nativeQuery = true)
    List<OrderReportDTO> getCustomReport(@Param("start") String start, @Param("end") String end);

    // Phương thức đếm số đơn hàng mới trong 24 giờ qua
    @Query(value = "SELECT COUNT(*) FROM orders WHERE order_date >= NOW() - INTERVAL 1 DAY", nativeQuery = true)
    int countNewOrdersInLast24Hours();
}
