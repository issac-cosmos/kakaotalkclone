package com.example.user.oauth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 15;   // 15 minutes
    private final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7; // 7 days
    public String generateAccessToken(String memberId) {
        return generateToken(memberId, ACCESS_TOKEN_VALIDITY);
    }

    public String generateRefreshToken(String memberId) {
        return generateToken(memberId, REFRESH_TOKEN_VALIDITY);
    }
    private String generateToken(String userId, long vaildity){
        Map<String , Object> Claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(Claims)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ vaildity))
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean vaildateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getUserIdFromToken(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
