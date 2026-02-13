package com.example.user.service;

import com.example.user.domain.Friendship;
import com.example.user.domain.FriendshipStatus;
import com.example.user.domain.User;
import com.example.user.dtos.LoginRequest;
import com.example.user.dtos.UserCreateDto;
import com.example.user.dtos.UserProfileDto;
import com.example.user.oauth.config.JwtUtil;
import com.example.user.oauth.dto.LoginResponse;
import com.example.user.oauth.dto.RegisterRequest;
import com.example.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public Long createUser(UserCreateDto dto) {
        User user = User.create(dto.birth(), dto.email(), dto.searchId());
        return userRepository.save(user).getId();
    }

    public User userFindById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("userFindById - id : " + id + " -> 없는 ID입니다."));
        return user;
    }

    public User getUserProfile(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("getUserProfile - id : " + id + " -> not found user"));
        return user;
    }

    // user 확인검증
//    public Boolean validateUser(LoginRequest loginRequest){
//        User user = userRepository.findByUserId(loginRequest.getId())
//                .orElseThrow(()->new UsernameNotFoundException("User not found with Id : "+loginRequest.getId()));
//        return passwordEncoder.matches(loginRequest.getPw(), user.getPassword());
//    }
    // user 회원가입
    public void registerUser(RegisterRequest registerRequest) {
//        User user = new User();
//        user.toEntity(registerRequest.getRegisterId(),passwordEncoder.encode(registerRequest.getRegisterPw()));
//        userRepository.save(user);
//    }
//    public LoginResponse validateUser(LoginRequest loginRequest) {
//        User user = userRepository.findByUserId(loginRequest.getId())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + loginRequest.getId()));
//        if (passwordEncoder.matches(loginRequest.getPw(), user.getPassword())) {
//            String accessToken = jwtUtil.generateAccessToken(user.getUserId());
//            String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());
//            return new LoginResponse(accessToken, refreshToken);
//        } else {
//            throw new RuntimeException("Invalid credentials");
//        }
//    }

    }
}
