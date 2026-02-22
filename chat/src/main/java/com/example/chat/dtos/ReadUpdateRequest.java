package com.example.chat.dtos;

import jakarta.validation.constraints.Min;

public record ReadUpdateRequest(
        @Min(0) long lastReadMessageId
) {}
