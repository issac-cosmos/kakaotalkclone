package com.example.chat.dtos;

import java.time.Instant;

public record ChatMessageResponse(
        Long id,
        String roomId,
        String messageId,
        String senderId,
        String content,
        Instant createdAt
) {}