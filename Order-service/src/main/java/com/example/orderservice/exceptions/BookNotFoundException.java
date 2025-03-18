package com.example.orderservice.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {
        super("Book is not found");
    }
}

