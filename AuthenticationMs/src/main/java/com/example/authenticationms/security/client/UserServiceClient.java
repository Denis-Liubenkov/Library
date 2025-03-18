package com.example.authenticationms.security.client;

import com.example.authenticationms.security.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@FeignClient(name = "User-service", url = "http://User-service:8081")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    Optional<User> getUserById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);
}





