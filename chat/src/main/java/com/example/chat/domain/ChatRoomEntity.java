package com.example.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomEntity {

    @Id
    @Column(name = "room_id", length = 64)
    private String roomId;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "type", length = 20, nullable = false)
    private String type; // "DM", "GROUP" 등

    @Column(name = "last_message_id")
    private Long lastMessageId;

    @Column(name = "last_message_at")
    private Instant lastMessageAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public static ChatRoomEntity create(String roomId, String type, String title) {
        ChatRoomEntity r = new ChatRoomEntity();
        r.roomId = roomId;
        r.type = type;
        r.title = title;
        r.createdAt = Instant.now();
        r.updatedAt = r.createdAt;
        return r;
    }

    public void touchLastMessage(Long messageId, Instant messageAt) {
        this.lastMessageId = messageId;
        this.lastMessageAt = messageAt;
        this.updatedAt = Instant.now();
    }
}