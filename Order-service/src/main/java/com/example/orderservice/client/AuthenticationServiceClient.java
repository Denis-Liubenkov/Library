package com.example.orderservice.client;

import com.example.orderservice.domain.CustomUserDetails;
import com.example.orderservice.domain.UserClaims;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "AuthenticationMs", url = "http://AuthenticationMs:8084")
public interface AuthenticationServiceClient {

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
