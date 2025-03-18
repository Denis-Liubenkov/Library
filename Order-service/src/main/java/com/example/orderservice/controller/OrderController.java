package com.example.orderservice.controller;

import com.example.orderservice.Producer.OrderProducer;
import com.example.orderservice.domain.*;
import com.example.orderservice.exceptions.BookNotFoundException;
import com.example.orderservice.exceptions.OrderNotFoundException;
import com.example.orderservice.exceptions.UserNotFoundException;
import com.example.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private final OrderProducer orderProducer;

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService, OrderProducer orderProducer) {
        this.orderService = orderService;
        this.orderProducer = orderProducer;
    }

    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/user/{orderId}")
    public ResponseEntity<User> getUserById(@PathVariable("orderId") Long orderId, @RequestHeader("Authorization") String token) {
        User user = orderService.getUserByOrderId(orderId, token).orElseThrow(UserNotFoundException::new);
        log.info("User with orderId :" + orderId + " is found!");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}/{bookId}")
    public ResponseEntity<Order> getOrderByUserIdAndBookId(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId, @RequestHeader("Authorization") String token) {
        Order order = orderService.getOrderByUserIdAndBookId(userId, bookId, token).orElseThrow(OrderNotFoundException::new);
        log.info("Order with userId :" + userId + " and with bookId :" + bookId + " is found!");
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/book/{orderId}")
    public ResponseEntity<Book> getBookByOrderId(@PathVariable("orderId") Long orderId, @RequestHeader("Authorization") String token) {
        Book book = orderService.getBookByOrderId(orderId, token).orElseThrow(BookNotFoundException::new);
        log.info("Book with orderId :" + orderId + " is found!");
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable("orderId") Long orderId, @RequestHeader("Authorization") String token) {
        return orderService.getOrder(orderId, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String token) {
        Order order = orderService.createOrder(orderRequest.getUserId(), orderRequest.getBookId(),orderRequest.getQuantity(), token);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setBookId(order.getBookId());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setOrderDate(order.getOrderDate());
        orderProducer.sendMessage(orderDTO);
        log.info("Order with user`s id: " + orderRequest.getUserId() + " is created!");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orderList = orderService.getAllOrders();
        if (orderList.isEmpty()) {
            log.info("List of orders are not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info("List of orders are found!");
            return new ResponseEntity<>(orderList, HttpStatus.OK);
        }
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrder(orderId);
        log.info("Order with id: " + orderId + " is deleted!");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/{orderId}")
    public ResponseEntity<HttpStatus> updateOrder(@PathVariable("orderId") Long orderId, @RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String token) {
        orderService.updateOrder(orderId, orderRequest.getUserId(), orderRequest.getBookId(), token);
        log.info("Order with id: " + orderId + " is updated!");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

