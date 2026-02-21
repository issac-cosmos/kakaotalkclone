//package com.example.gateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(ex -> ex
//                        .pathMatchers("/ws/**").permitAll()
//                        .pathMatchers("/api/rooms/**").permitAll()
//                        .anyExchange().permitAll()
//                )
//                .build();
//    }
//}