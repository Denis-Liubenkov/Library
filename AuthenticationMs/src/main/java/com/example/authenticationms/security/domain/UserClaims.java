package com.example.authenticationms.security.domain;

import lombok.Data;

@Data
public class UserClaims {

    private String login;
    private Long userId;

    public UserClaims(String login, Long userId) {
        this.login = login;
        this.userId = userId;
    }
}
