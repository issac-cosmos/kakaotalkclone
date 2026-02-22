package com.example.chat.dtos;

import java.time.Instant;

public record RoomListItem(
        String roomId,
        String title,
        String type,
        Long lastMessageId,
        String lastMessageContent,
        Instant lastMessageAt,
        long unreadCount
) {}