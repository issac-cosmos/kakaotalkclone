package com.example.chat.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "chat_message",
        indexes = {
                @Index(name = "idx_room_created", columnList = "room_id, created_at"),
                @Index(name = "idx_room_id_id", columnList = "room_id, id")
        },
        uniqueConstraints = @UniqueConstraint(name = "uk_room_message", columnNames = {"room_id", "message_id"})
)
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="room_id", nullable=false, length=64)
    private String roomId;

    @Column(name="message_id", nullable=false, length=64)
    private String messageId;

    @Column(name="sender_id", nullable=false, length=64)
    private String senderId;

    @Column(name="content", nullable=false, length=2000)
    private String content;

    @Column(name="created_at", nullable=false)
    private Instant createdAt;

    public static ChatMessageEntity of(String roomId, String messageId, String senderId, String content) {
        ChatMessageEntity e = new ChatMessageEntity();
        e.roomId = roomId;
        e.messageId = messageId;
        e.senderId = senderId;
        e.content = content;
        e.createdAt = Instant.now();
        return e;
    }
}
