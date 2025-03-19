package com.example.orderservice.Producer;

import com.example.orderservice.domain.OrderDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(OrderDTO orderDTO) {
        String TOPIC = "new-orders";
        kafkaTemplate.send(TOPIC, orderDTO);
    }
}
