package com.example.orderservice.repository;

import com.example.orderservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @NonNull
    List<Order> findAll();

    @NonNull
    Optional<Order> findById(@NonNull Long orderId);

    @NonNull
    Optional<Order> findByUserIdAndBookId(Long userId, Long bookId);

    void deleteById(@NonNull Long orderId);
}
