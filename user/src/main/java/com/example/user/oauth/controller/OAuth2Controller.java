package com.example.oauth.controller;

import com.example.oauth.service.GoogleOAuth2Service;
import com.example.user.domain.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class OAuth2Controller {

    private final GoogleOAuth2Service oAuth2Service;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Google OAuth 로그인 시작
     */
    @GetMapping("/google")
    public ResponseEntity<?> googleLogin(HttpSession session) {
        try {
            String authUrl = oAuth2Service.getAuthorizationUrl(session);
            return ResponseEntity.ok(Map.of("authUrl", authUrl));
        } catch (Exception e) {
            log.error("Error initiating Google OAuth", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to initiate OAuth"));
        }
    }

    /**
     * Google OAuth 콜백 처리
     */
    @GetMapping("/google/callback")
    public ResponseEntity<?> googleCallback(
            @RequestParam String code,
            @RequestParam String state,
            HttpSession session,
            HttpServletResponse response) {

        try {
            // State 검증
            String sessionState = (String) session.getAttribute("oauth_state");
            if (sessionState == null || !sessionState.equals(state)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid state parameter"));
            }

            // 세션에서 state 제거
            session.removeAttribute("oauth_state");

            // Authorization Code를 Access Token으로 교환
            OAuth2TokenResponse tokenResponse = oAuth2Service.exchangeCodeForTokens(code);

            // 사용자 정보 조회
            GoogleUserInfo userInfo = oAuth2Service.getUserInfo(tokenResponse.getAccessToken());

            // 사용자 생성 또는 업데이트
            User user = oAuth2Service.createOrUpdateUser(userInfo);

            // OAuth 토큰 저장
            oAuth2Service.saveTokens(user, tokenResponse);

            // JWT 토큰 생성
            String jwtToken = jwtTokenProvider.createToken(user.getEmail(), List.of("USER"));

            // JWT를 HttpOnly 쿠키로 설정
            Cookie jwtCookie = new Cookie("jwt_token", jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true); // HTTPS에서만
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
            response.addCookie(jwtCookie);

            // 프론트엔드로 리다이렉트
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/dashboard"))
                    .build();

        } catch (OAuth2Exception e) {
            log.error("OAuth2 callback error", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during OAuth callback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Authentication failed"));
        }
    }

    /**
     * 현재 사용자 정보 조회
     */
    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .picture(user.getPicture())
                    .build();

            return ResponseEntity.ok(Map.of("user", userDto));

        } catch (Exception e) {
            log.error("Error getting current user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get user information"));
        }
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // JWT 쿠키 삭제
        Cookie jwtCookie = new Cookie("jwt_token", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}