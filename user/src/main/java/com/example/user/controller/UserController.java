package com.example.user.controller;

import com.example.user.domain.Friendship;
import com.example.user.domain.User;
import com.example.user.dtos.LoginRequest;
import com.example.user.dtos.UserCreateDto;
import com.example.user.dtos.UserProfileDto;
import com.example.user.oauth.dto.LoginResponse;
import com.example.user.oauth.dto.RegisterRequest;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //유저 생성 create
    @PostMapping("/create")
    public ResponseEntity<User> userCreate(@RequestBody UserCreateDto dto){
        User user = userService.userCreate(dto);
        return ResponseEntity.ok(user);
    }
    //관리자 유저 조회용
    @GetMapping("/admin/{id}")
    public ResponseEntity<User> userFindById(@PathVariable Long id){
        User user = userService.userFindById(id);
        return ResponseEntity.ok(user);
    }
    //유저끼리 유저 조회
    @GetMapping("/search/{id}")
    public UserProfileDto userProfile(@PathVariable Long id){
        User user = userService.getUserProfile(id);
        return user.profileFromEntity();
    }

    //로그인
//    @PostMapping("/login")
//    public String  login(@RequestBody LoginRequest loginRequest){
//        boolean validated = userService.validateUser(loginRequest);
//        return validated ? "success" : "fail";
//    }
    //회원가입
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest registerRequest){
        userService.registerUser(registerRequest);
        return "Registered successfully";
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.validateUser(loginRequest);
    }
    //리프레시토큰발급

    //내친구 목록조회


//    @GetMapping("/friendList")
//    public ResponseEntity<List> friendList(){
//
//    }
}
