package com.example.chat.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "room_participant")
@IdClass(RoomParticipantId.class)
public class RoomParticipant {

    @Id
    @Column(name = "room_id", nullable = false)
    private String roomId;

    @Id
    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    @Column(name = "last_read_message_id", nullable = false)
    private long lastReadMessageId;

    @Column(name = "last_read_at")
    private Instant lastReadAt;

    public static RoomParticipant create(String roomId, String userId) {
        RoomParticipant p = new RoomParticipant();
        p.roomId = roomId;
        p.userId = userId;
        p.lastReadMessageId = 0;
        p.lastReadAt = null;
        return p;
    }
}