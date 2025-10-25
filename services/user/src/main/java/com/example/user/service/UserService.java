package com.example.user.service;

import com.example.user.domain.User;
import com.example.user.dtos.UserCreateDto;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Long userCreate(UserCreateDto dto){
        User userId = userRepository.save(dto.toEntity);
        return userId;
    }
}
