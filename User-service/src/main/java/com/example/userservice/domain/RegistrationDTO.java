package com.example.userservice.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDTO {

    @NotBlank(message = "Firstname is required")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "User login is required")
    @Size(min = 4, max = 20, message = "User login must be between 4 and 20 characters")
    private String userLogin;

    @NotBlank(message = "User password is required")
    @Size(min = 4, message = "User password must be at least 4 characters")
    private String userPassword;
}
