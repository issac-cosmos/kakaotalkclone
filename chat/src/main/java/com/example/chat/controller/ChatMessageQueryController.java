package com.example.chat.controller;

import com.example.chat.domain.ChatMessageEntity;
import com.example.chat.dtos.ChatMessageResponse;
import com.example.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class ChatMessageQueryController {

    private final ChatMessageRepository repo;

    @GetMapping("/{roomId}/messages")
    public List<ChatMessageResponse> getMessages(
            @PathVariable String roomId,
            @RequestParam(required = false) Long cursorId
    ) {
        List<ChatMessageEntity> list = (cursorId == null)
                ? repo.findTop50ByRoomIdOrderByIdDesc(roomId)
                : repo.findTop50ByRoomIdAndIdLessThanOrderByIdDesc(roomId, cursorId);

        return list.stream().map(this::toDto).toList();
    }

    private ChatMessageResponse toDto(ChatMessageEntity e) {
        return new ChatMessageResponse(
                e.getId(), e.getRoomId(), e.getMessageId(), e.getSenderId(), e.getContent(), e.getCreatedAt()
        );
    }
}