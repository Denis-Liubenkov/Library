package com.example.booksservice.Consumer;

import com.example.booksservice.domain.Book;
import com.example.booksservice.domain.OrderDTO;
import com.example.booksservice.repository.BooksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderConsumer {

    private final BooksRepository booksRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;

    public OrderConsumer(BooksRepository booksRepository, KafkaTemplate<String, OrderDTO> kafkaTemplate) {
        this.booksRepository = booksRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "new-orders", groupId = "${spring.application.name}-group")
    public void consume(OrderDTO orderDTO) {
        LOGGER.info(String.format("Order consumed -> %s", orderDTO));
        Long bookId = orderDTO.getBookId();
        Integer quantityOrdered = orderDTO.getQuantity();
        LOGGER.info("Updating book quantity bookId: " + bookId);
        Optional<Book> bookOptional = booksRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            int currentQuantity = book.getQuantity();
            int newQuantity = currentQuantity - quantityOrdered;
            if (newQuantity >= 0) {
                book.setQuantity(newQuantity);
                booksRepository.save(book);
                LOGGER.info("Book quantity updated successfully. Book ID: " + bookId + ", New quantity: " + newQuantity);
            } else {
                LOGGER.warn("Not enough books in stock. Book ID: " + bookId + ", Ordered quantity: " + quantityOrdered + ", Current quantity: " + currentQuantity);
                String ORDER_CANCELLATION_TOPIC = "order-cancellation";
                LOGGER.info("Sending cancellation message to topic: " + ORDER_CANCELLATION_TOPIC + ", orderDTO: " + orderDTO);
                kafkaTemplate.send(ORDER_CANCELLATION_TOPIC, orderDTO);
            }
            {
                LOGGER.error("Book not found. Book ID: " + bookId);
                String ORDER_CANCELLATION_TOPIC = "order-cancellation";
                LOGGER.info("Sending cancellation message to topic: " + ORDER_CANCELLATION_TOPIC + ", orderDTO: " + orderDTO);
                kafkaTemplate.send(ORDER_CANCELLATION_TOPIC, orderDTO);
            }
        }
    }
}
