package com.example.chat.read;

public record ChatReadEvent(
        String roomId,
        String userId,
        long lastReadMessageId,
        String eventId,     // UUID (멱등)
        long clientAtEpochMs
) {}
