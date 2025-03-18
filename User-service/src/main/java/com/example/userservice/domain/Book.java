package com.example.userservice.domain;

import lombok.Data;

@Data
public class Book {
    private Long id;
    private String author;
    private String genre;
    private String publisher;
    private String title;
    private String description;
    private String fileId;
    private Long userId;
}

