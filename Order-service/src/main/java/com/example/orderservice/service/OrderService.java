package com.example.orderservice.service;

import com.example.orderservice.client.AuthenticationServiceClient;
import com.example.orderservice.client.BookServiceClient;
import com.example.orderservice.client.UserServiceClient;
import com.example.orderservice.domain.Book;
import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.domain.User;
import com.example.orderservice.exceptions.OrderNotFoundException;
import com.example.orderservice.exceptions.UserNotFoundException;
import com.example.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final UserServiceClient userServiceClient;
    private final BookServiceClient bookServiceClient;

    private final AuthenticationServiceClient authenticationServiceClient;
    private final OrderRepository orderRepository;

    public OrderService(UserServiceClient userServiceClient, BookServiceClient bookServiceClient, AuthenticationServiceClient authenticationServiceClient, OrderRepository orderRepository) {
        this.userServiceClient = userServiceClient;
        this.bookServiceClient = bookServiceClient;
        this.authenticationServiceClient = authenticationServiceClient;
        this.orderRepository = orderRepository;
    }

    @CircuitBreaker(name = "User-service", fallbackMethod = "fallbackGetUserByOrderId")
    public Optional<User> getUserByOrderId(Long orderId, String token) {
        if (!authenticationServiceClient.validateToken(token)) {
            throw new RuntimeException("Access denied: No access to user");
        }
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException();
        }
        return userServiceClient.getUserById(order.get().getUserId(), token);
    }

    @CircuitBreaker(name = "books-service", fallbackMethod = "fallbackGetBookByOrderId")
    public Optional<Book> getBookByOrderId(Long orderId, String token) {
        if (!authenticationServiceClient.validateToken(token)) {
            throw new RuntimeException("Access denied: No access to book");
        }
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException();
        }
        return bookServiceClient.getBookById(order.get().getBookId(), token);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(Long userId, Long bookId, Integer quantity, String token) {
        if (!authenticationServiceClient.validateToken(token)) {
            throw new RuntimeException("Access denied: No access to create order");
        }
        Optional<User> userOptional = userServiceClient.getUserById(userId, token);
        if (userOptional.isPresent()) {
            Order order = new Order();
            order.setUserId(userId);
            order.setBookId(bookId);
            order.setQuantity(quantity);
            order.setStatus(OrderStatus.CREATED);
            order.setOrderDate(LocalDateTime.now());
            orderRepository.save(order);
            User user = userOptional.get();
            user.setOrderId(order.getOrderId());
            userServiceClient.updateUser(user, userId, token);
            return order;
        }
        throw new UserNotFoundException();
    }

    public Optional<User> fallbackGetUserByOrderId(Long orderId, Throwable throwable) {
        System.err.println("Fallback called for getUser due to: " + orderId + throwable.getMessage());
        return Optional.empty();
    }

    public Optional<Book> fallbackGetBookByOrderId(Long orderId, Throwable throwable) {
        System.err.println("Fallback called for getBook due to: " + orderId + throwable.getMessage());
        return Optional.empty();
    }

    public Optional<Order> getOrder(Long orderId, String token) {
        if (!authenticationServiceClient.validateToken(token)) {
            throw new RuntimeException("Access denied: No access to order");
        }
        return orderRepository.findById(orderId);
    }

    public Optional<Order> getOrderByUserIdAndBookId(Long userId, Long bookId, String token) {
        if (!authenticationServiceClient.validateToken(token)) {
            throw new RuntimeException("Access denied: No access to order");
        }
        return orderRepository.findByUserIdAndBookId(userId, bookId);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public void updateOrder(Long orderId, Long userId, Long bookId, String token) {
        if (!authenticationServiceClient.validateToken(token)) {
            throw new RuntimeException("Access denied: No access to update order");
        }
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new OrderNotFoundException();
        }
        Order order = orderOptional.get();
        order.setOrderId(order.getOrderId());
        order.setUserId(userId);
        order.setBookId(bookId);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);
    }
}
