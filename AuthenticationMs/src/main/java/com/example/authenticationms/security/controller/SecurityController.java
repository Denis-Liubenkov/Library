package com.example.authenticationms.security.controller;

import com.example.authenticationms.security.domain.*;
import com.example.authenticationms.security.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class SecurityController {
    private static final Logger log = LoggerFactory.getLogger(SecurityController.class);

    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> generateToken(@RequestBody AuthRequest authRequest) {
        String token = securityService.generateToken(authRequest);
        if (token.isBlank()) {
            log.info("User " + authRequest.getLogin() + " is authenticated unsuccessfully!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        log.info("User " + authRequest.getLogin() + " is successfully authenticated!");
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/credentials")
    public ResponseEntity<HttpStatus> saveCredentialsToDB(@RequestBody SecurityCredentials securityCredentials) {
        securityService.saveCredentials(securityCredentials);
        log.info("User with login : " + securityCredentials.getUserLogin() + " is saved!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        Boolean isValidated = securityService.validateToken(token);
        log.info("User with token : " + token + " is validated!");
        return ResponseEntity.ok(isValidated);
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getUserClaimsFromJwt")
    public ResponseEntity<UserClaims> getUserClaimsFromJwt(@RequestHeader("Authorization") String token) {
        UserClaims userClaims = securityService.getUserClaimsJwt(token);
        log.info("User with  : " + userClaims + " is found!");
        return ResponseEntity.ok(userClaims);
    }

    @GetMapping("/removePrefixBearer")
    public ResponseEntity<String> removePrefixBearer(@RequestHeader("Authorization") String token) {
        String bearerPrefix = securityService.removeBearerPrefix(token);
        return ResponseEntity.ok(bearerPrefix);
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/userDetails/{username}")
    public UserDetails loadUserByUsername(@PathVariable String username) {
        UserDetails userDetails = securityService.loadUserByUsername(username);
        return ResponseEntity.ok(userDetails).getBody();
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getLoginFromJwt")
    public ResponseEntity<String> getLoginFromJwt(@RequestHeader("Authorization") String token) {
        String login = securityService.getLoginFromJwt(token);
        log.info("User with  : " + login + " is found!");
        return ResponseEntity.ok(login);
    }
}
