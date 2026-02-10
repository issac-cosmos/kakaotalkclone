package com.example.user.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessTokenl;
    private String refreshToken;
}
