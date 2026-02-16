package com.example.chat.dtos;

//테스트 dto
public record ChatMessageEvent (
        String roomId,
        String messageId,
        String senderId,
        String content
){
}
