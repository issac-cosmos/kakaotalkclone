package com.example.user.dtos;

import com.example.user.domain.DelYN;
import com.example.user.domain.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public record UserCreateDto(
        @NotBlank @Email String email,
        @NotBlank @Size(max = 50) String searchId,
        @NotBlank @Size(min = 8, max = 8) String birth
) {
}
