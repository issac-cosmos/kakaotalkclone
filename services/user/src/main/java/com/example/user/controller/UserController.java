package com.example.user.controller;

import com.example.user.domain.User;
import com.example.user.dtos.UserCreateDto;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> userCreate(@RequestBody UserCreateDto dto){
        Long id = userService.userCreate(dto);
        return ResponseEntity.ok(HttpStatus.OK,id);
    }
}
