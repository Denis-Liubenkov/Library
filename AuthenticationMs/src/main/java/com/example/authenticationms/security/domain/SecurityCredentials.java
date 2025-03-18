package com.example.authenticationms.security.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Entity(name = "security_credentials")
@Component
public class SecurityCredentials {

    @Id
    @SequenceGenerator(name = "securityCredentialsSeq", sequenceName = "security_credentials_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "securityCredentialsSeq")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role userRole;

    @Column(name = "user_password")
    private String userPassword;
}
