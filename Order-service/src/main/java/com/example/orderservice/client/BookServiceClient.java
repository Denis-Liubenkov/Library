package com.example.orderservice.client;

import com.example.orderservice.domain.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "books-service", url = "http://books-service:8082")
public interface BookServiceClient {

    @GetMapping("/books/{bookId}")
    Optional<Book> getBookById(@PathVariable("bookId") Long bookId, @RequestHeader("Authorization") String token);
}


