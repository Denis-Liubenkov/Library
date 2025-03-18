package com.example.userservice.controller;

import com.example.userservice.domain.RegistrationDTO;
import com.example.userservice.domain.User;
import com.example.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @PreAuthorize("hasRole('USER') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token) {
        return userService.getUserById(id, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // @PreAuthorize("hasRole('USER') or #id == authentication.principal.id")
    @GetMapping("/book/{bookId}")
    public ResponseEntity<User> getUserByBookId(@PathVariable("bookId") Long bookId, @RequestHeader("Authorization") String token) {
        Optional<User> user = userService.getUserByBookId(bookId, token);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody RegistrationDTO registrationDTO) {
        userService.registerUser(registrationDTO);
        log.info("User with : " + registrationDTO.getFirstName() + " is registered!");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            log.info("List of users are not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info("List of users are found!");
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        log.info("User with id: " + id + " is deleted!");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user, @PathVariable("id") Long id, @RequestHeader("Authorization") String token) {
        userService.updateUser(user, token);
        log.info("User with id: " + id + " is updated!");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
