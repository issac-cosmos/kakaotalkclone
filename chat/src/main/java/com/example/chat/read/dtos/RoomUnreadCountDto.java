package com.example.chat.read.dtos;

public record RoomUnreadCountDto(
        String roomId,
        long unreadCount
) {}