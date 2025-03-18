package com.example.userservice.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class SecurityCredentials {

    private String userLogin;

    private String userPassword;

    private Role userRole;

    private Long userId;

    private Long id;
}
