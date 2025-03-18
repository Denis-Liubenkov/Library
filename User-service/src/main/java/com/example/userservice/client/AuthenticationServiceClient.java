package com.example.userservice.client;

import com.example.userservice.domain.CustomUserDetails;
import com.example.userservice.domain.SecurityCredentials;
import com.example.userservice.domain.UserClaims;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "AuthenticationMs", url = "http://AuthenticationMs:8084")
public interface AuthenticationServiceClient {

    @PostMapping("/auth/credentials")
    void saveCredentials(@RequestBody SecurityCredentials securityCredentials);

    @GetMapping("/auth/validate")
    Boolean validateToken(@RequestHeader("Authorization") String token);

    @GetMapping("/auth/getLoginFromJwt")
    String getLoginFromJwt(@RequestHeader("Authorization") String token);

    @GetMapping("/auth/userDetails/{username}")
    CustomUserDetails loadUserByUsername(@PathVariable String username);

    @GetMapping("/auth/getUserClaimsFromJwt")
    UserClaims getUserClaimsFromJwt(@RequestHeader("Authorization") String token);

    @GetMapping("/auth/removePrefixBearer")
    String removePrefixBearer(@RequestHeader("Authorization") String token);
}
