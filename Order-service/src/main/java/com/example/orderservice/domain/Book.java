package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class Book {
    @Id
    private Long id;
    private String author;
    private String genre;
    private String publisher;
    private String title;
    private String description;
    private String fileId;
    private Long userId;
}
