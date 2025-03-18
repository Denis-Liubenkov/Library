package com.example.userservice.service;

import com.example.userservice.client.AuthenticationServiceClient;
import com.example.userservice.client.BookServiceClient;
import com.example.userservice.domain.*;
import com.example.userservice.exceptions.BookNotFoundException;
import com.example.userservice.exceptions.UserNotFoundException;
import com.example.userservice.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BookServiceClient bookServiceClient;

    private final User user;

    private final AuthenticationServiceClient authenticationServiceClient;


    public UserService(UserRepository userRepository, BookServiceClient bookServiceClient, User user, AuthenticationServiceClient authenticationServiceClient) {
        this.userRepository = userRepository;
        this.user = user;
        this.bookServiceClient = bookServiceClient;
        this.authenticationServiceClient = authenticationServiceClient;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id, String token) {
        if (!authenticationServiceClient.validateToken(token)) {
            throw new RuntimeException("Access denied: No access to user");
        }
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            Long userId = byId.get().getId();
            if (userId.equals(id)) {
                return byId;
            }
            return Optional.empty();
        }
        throw new UserNotFoundException();
    }

    @CircuitBreaker(name = "User-service", fallbackMethod = "fallbackGetUserByBookId")
    public Optional<User> getUserByBookId(Long bookId, String token) {
        if (!authenticationServiceClient.validateToken(token)) {
            throw new RuntimeException("Access denied: No access to get user by bookId");
        }
        Optional<Book> books = bookServiceClient.getBookById(bookId, token);
        if (books.isEmpty()) {
            throw new BookNotFoundException();
        }
        Optional<User> user = userRepository.findById(books.get().getUserId());
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        return user;
    }

    public Optional<User> fallbackGetUserByBookId(Long bookId, Throwable throwable) {
        System.err.println("Fallback response: Book service with bookId " + bookId + "is currently unavailable for user ID. Error: " + throwable.getMessage());
        return Optional.empty();
    }

    @Transactional(rollbackFor = Exception.class)
    public void registerUser(RegistrationDTO registrationDTO) {
        user.setFirstName(registrationDTO.getFirstName());
        user.setCreationDate(LocalDate.now());
        user.setLastName(registrationDTO.getLastName());
        user.setEmail(registrationDTO.getEmail());
        user.setRole(Role.USER);
        userRepository.save(user);

        SecurityCredentials securityCredentials = new SecurityCredentials();
        securityCredentials.setUserId(user.getId());
        securityCredentials.setUserLogin(registrationDTO.getUserLogin());
        securityCredentials.setUserRole(Role.USER);
        securityCredentials.setUserPassword(registrationDTO.getUserPassword());
        authenticationServiceClient.saveCredentials(securityCredentials);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUser(User user, String token) {
        if (!authenticationServiceClient.validateToken(token)) {
            throw new RuntimeException("Access denied: No access to update user");
        }
        user.setId(user.getId());
        user.setFirstName(user.getFirstName());
        user.setCreationDate(LocalDate.now());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        user.setRole(user.getRole());
        user.setBookId(user.getBookId());
        user.setOrderId(user.getOrderId());
        userRepository.save(user);
    }
}

