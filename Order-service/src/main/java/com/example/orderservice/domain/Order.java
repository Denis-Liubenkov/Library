package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Long userId;
    private Long bookId;
    private Integer quantity;
    private OrderStatus status;
    private LocalDateTime orderDate;
}

