package com.example.orderservice.client;

import com.example.orderservice.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "User-service", url = "http://User-service:8081")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    Optional<User> getUserById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);

    @PutMapping("/users/{id}")
    void updateUser(@RequestBody User user, @PathVariable("id") Long id, @RequestHeader("Authorization") String token);
}





