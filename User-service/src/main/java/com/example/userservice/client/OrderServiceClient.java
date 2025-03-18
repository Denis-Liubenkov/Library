package com.example.userservice.client;

import com.example.userservice.domain.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@FeignClient(name = "Order-service", url = "http://Order-service:8083")
public interface OrderServiceClient {

    @GetMapping("/orders/{id}")
    Optional<Order> getOrderById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);
}


