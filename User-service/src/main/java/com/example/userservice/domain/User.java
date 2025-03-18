package com.example.userservice.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Component
@Table(name = "list_of_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Long orderId;
    private Long bookId;
}
