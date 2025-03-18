package com.example.booksservice.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private Long bookId;
    private Integer quantity;
    private LocalDateTime orderDate;
}
