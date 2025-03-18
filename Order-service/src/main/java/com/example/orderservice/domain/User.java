package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    @Id
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate creationDate;
    private Role role;
    private Long orderId;
    private Long bookId;
}
