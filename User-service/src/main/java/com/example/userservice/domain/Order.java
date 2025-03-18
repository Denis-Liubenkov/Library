package com.example.userservice.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime orderDate;
}

