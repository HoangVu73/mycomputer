package com.example.my_computer.dto;

import java.math.BigDecimal;

public interface OrderReportDTO {
    String getPeriod();
    Long getOrderCount();
    BigDecimal getTotalRevenue();
}
