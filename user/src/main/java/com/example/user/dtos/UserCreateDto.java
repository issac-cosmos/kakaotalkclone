package com.example.user.dtos;

import com.example.user.domain.DelYN;
import com.example.user.domain.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    private String email;
    private String password;
    private String searchId;
    private LocalDateTime createAt;


    //toEntity
    public User toEntity(){
        return User.builder()
                .email(this.email).password(this.password)
                .searchId(this.searchId)
                .createAt(LocalDateTime.now())
                .build();
    }
}
