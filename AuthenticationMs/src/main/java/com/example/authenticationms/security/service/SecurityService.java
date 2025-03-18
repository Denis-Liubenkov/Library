package com.example.authenticationms.security.service;

import com.example.authenticationms.security.CustomUserDetailService;
import com.example.authenticationms.security.JwtUtils;
import com.example.authenticationms.security.domain.*;
import com.example.authenticationms.security.repository.SecurityCredentialsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Component
public class SecurityService {

    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailService customUserDetailService;

    private final SecurityCredentialsRepository securityCredentialsRepository;

    public SecurityService(JwtUtils jwtUtils, PasswordEncoder passwordEncoder, CustomUserDetailService customUserDetailService, SecurityCredentialsRepository securityCredentialsRepository) {
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailService = customUserDetailService;
        this.securityCredentialsRepository = securityCredentialsRepository;
    }

    public String generateToken(AuthRequest authRequest) {
        Optional<SecurityCredentials> securityCredentials = securityCredentialsRepository.findByUserLogin(authRequest.getLogin());
        if (securityCredentials.isPresent() && passwordEncoder.matches(authRequest.getPassword(), securityCredentials.get().getUserPassword())) {
            log.info("Token is successfully generated!");
            SecurityCredentials credentials = securityCredentials.get();
            UserClaims userClaims = new UserClaims(credentials.getUserLogin(), credentials.getUserId());
            return jwtUtils.generateJwtToken(userClaims);
        }
        log.info("Token is not generated!");
        return "";
    }

    public Boolean validateToken(String token) {
        String removeBearerPrefix = removeBearerPrefix(token);
        return jwtUtils.validateToken(removeBearerPrefix);
    }

    public UserClaims getUserClaimsJwt(String token) {
        return jwtUtils.getUserClaimsFromJwt(token);
    }

    public String getLoginFromJwt(String token) {
        return jwtUtils.getLoginFromJwt(token);
    }

    public String removeBearerPrefix(String token) {
        return jwtUtils.removeBearerPrefix(token);
    }

    public UserDetails loadUserByUsername(String username) {
        return customUserDetailService.loadUserByUsername(username);
    }

    public void saveCredentials(SecurityCredentials securityCredentials) {
        securityCredentials.setUserLogin(securityCredentials.getUserLogin());
        securityCredentials.setUserPassword((passwordEncoder.encode(securityCredentials.getUserPassword())));
        securityCredentials.setUserRole(Role.USER);
        securityCredentials.setUserId(securityCredentials.getUserId());
        securityCredentialsRepository.save(securityCredentials);
    }
}

