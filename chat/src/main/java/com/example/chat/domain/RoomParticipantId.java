package com.example.chat.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoomParticipantId implements Serializable {
    private String roomId;
    private String userId;
}