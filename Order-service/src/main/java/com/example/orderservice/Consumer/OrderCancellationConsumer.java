package com.example.orderservice.Consumer;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderDTO;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;


@Service
public class OrderCancellationConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancellationConsumer.class);

    private final OrderRepository orderRepository;

    public OrderCancellationConsumer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "order-cancellation", groupId = "${spring.application.name}-group")
    public void consume(OrderDTO orderDTO) {
        LOGGER.info(String.format("Received cancellation request for order: %s", orderDTO));
        Long orderId = orderDTO.getOrderId();
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getStatus() != OrderStatus.CANCELLED && order.getStatus() != OrderStatus.COMPLETED) {
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
                LOGGER.info("Order with ID: " + orderId + " cancelled successfully.");
            } else {
                LOGGER.warn("Order with ID: " + orderId + " is already cancelled or completed. Ignoring cancellation request.");
            }
        } else {
            LOGGER.error("Order with ID: " + orderId + " not found. Cannot cancel order.");
        }
    }
}
