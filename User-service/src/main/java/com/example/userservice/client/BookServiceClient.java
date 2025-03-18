package com.example.userservice.client;

import com.example.userservice.domain.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@FeignClient(name = "books-service", url = "http://books-service:8082")
public interface BookServiceClient {

    @GetMapping("/books/{bookId}")
    Optional<Book> getBookById(@PathVariable("bookId") Long bookId, @RequestHeader("Authorization") String token);
}



