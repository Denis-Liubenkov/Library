package com.example.orderservice.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private Long bookId;
    private Integer quantity;
    private OrderStatus status;
    private LocalDateTime orderDate;
}
