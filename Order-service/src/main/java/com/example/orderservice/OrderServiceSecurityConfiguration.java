package com.example.orderservice;

import com.example.orderservice.filter.OrderFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
//@EnableMethodSecurity
public class OrderServiceSecurityConfiguration {

    private final OrderFilter orderFilter;


    public OrderServiceSecurityConfiguration(OrderFilter orderFilter) {
        this.orderFilter = orderFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.GET, "/orders").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/orders/**").hasRole("USER")
                                .requestMatchers(HttpMethod.POST, "/orders").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/orders/{id}").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/orders/{id}").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(orderFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
