
package com.example.oauth.service;

import com.example.oauth.config.OAuth2Config;
import com.example.oauth.domain.OAuthToken;
import com.example.oauth.dto.GoogleUserInfo;
import com.example.oauth.dto.OAuth2TokenResponse;
import com.example.user.domain.User;
import com.example.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuth2Service {

    private final OAuth2Config oAuth2Config;
    private final WebClient webClient;
    private final UserRepository userRepository;
    private final OAuthTokenRepository tokenRepository;
    private final SecurityService securityService;


/**
     * Google OAuth 인증 URL 생성
     */

    public String getAuthorizationUrl(HttpSession session) {
        // CSRF 방지를 위한 state 생성
        String state = generateSecureRandomString(32);
        session.setAttribute("oauth_state", state);

        return UriComponentsBuilder
                .fromUriString(oAuth2Config.getAuthorizationUri())
                .queryParam("client_id", oAuth2Config.getClientId())
                .queryParam("redirect_uri", oAuth2Config.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "profile email")
                .queryParam("access_type", "offline")  // Refresh Token 획득
                .queryParam("prompt", "consent")       // 항상 consent screen 표시
                .queryParam("state", state)
                .build()
                .toUriString();
    }

/**
     * Authorization Code를 Access Token으로 교환
     */

    public OAuth2TokenResponse exchangeCodeForTokens(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", oAuth2Config.getClientId());
        params.add("client_secret", oAuth2Config.getClientSecret());
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", oAuth2Config.getRedirectUri());

        try {
            return webClient.post()
                    .uri(oAuth2Config.getTokenUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(params))
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> {
                        log.error("Token exchange failed with status: {}", response.statusCode());
                        return Mono.error(new OAuth2Exception("Token exchange failed"));
                    })
                    .bodyToMono(OAuth2TokenResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Error during token exchange", e);
            throw new OAuth2Exception("Failed to exchange authorization code", e);
        }
    }

/**
     * Access Token으로 사용자 정보 조회
     */

    public GoogleUserInfo getUserInfo(String accessToken) {
        try {
            return webClient.get()
                    .uri(oAuth2Config.getUserInfoUri())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> {
                        log.error("User info request failed with status: {}", response.statusCode());
                        return Mono.error(new OAuth2Exception("Failed to get user info"));
                    })
                    .bodyToMono(GoogleUserInfo.class)
                    .block();
        } catch (Exception e) {
            log.error("Error getting user info", e);
            throw new OAuth2Exception("Failed to get user information", e);
        }
    }
/**
     * Refresh Token으로 새로운 Access Token 획득
     */

    public OAuth2TokenResponse refreshAccessToken(String refreshToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", oAuth2Config.getClientId());
        params.add("client_secret", oAuth2Config.getClientSecret());
        params.add("refresh_token", refreshToken);
        params.add("grant_type", "refresh_token");

        try {
            return webClient.post()
                    .uri(oAuth2Config.getTokenUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(params))
                    .retrieve()
                    .bodyToMono(OAuth2TokenResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Error refreshing access token", e);
            throw new OAuth2Exception("Failed to refresh access token", e);
        }
    }


/**
     * 사용자 생성 또는 업데이트
     */

    @Transactional
    public User createOrUpdateUser(GoogleUserInfo userInfo) {
        Optional<User> existingUser = userRepository.findByEmail(userInfo.getEmail());

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(userInfo.getName());
            user.setPicture(userInfo.getPicture());
            return userRepository.save(user);
        } else {
            User newUser = new User();
            newUser.setEmail(userInfo.getEmail());
            newUser.setName(userInfo.getName());
            newUser.setPicture(userInfo.getPicture());
            newUser.setProvider("google");
            newUser.setProviderId(userInfo.getId());
            return userRepository.save(newUser);
        }
    }


/**
     * OAuth 토큰 저장
     */

    @Transactional
    public void saveTokens(User user, OAuth2TokenResponse tokenResponse) {
        // 기존 토큰 삭제
        tokenRepository.deleteByUserAndProvider(user, "google");

        // 새 토큰 저장 (암호화)
        OAuthToken token = new OAuthToken();
        token.setUser(user);
        token.setProvider("google");
        token.setAccessToken(securityService.encrypt(tokenResponse.getAccessToken()));

        if (tokenResponse.getRefreshToken() != null) {
            token.setRefreshToken(securityService.encrypt(tokenResponse.getRefreshToken()));
        }

        token.setExpiresAt(LocalDateTime.now().plusSeconds(tokenResponse.getExpiresIn()));
        tokenRepository.save(token);
    }

    private String generateSecureRandomString(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
