package com.example.chat.dtos;

public record RoomBadgeEvent(
        String roomId,
        long deltaUnread,        // +1 / -1 / 0
        Long lastMessageId,
        String lastMessageContent,
        long serverAtEpochMs
) {}