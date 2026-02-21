package com.example.chat.repository;

import com.example.chat.domain.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    boolean existsByRoomIdAndMessageId(String roomId, String messageId);

    // 커서 페이징: 특정 id보다 작은 것 최신순 N개
    List<ChatMessageEntity> findTop50ByRoomIdAndIdLessThanOrderByIdDesc(String roomId, Long cursorId);

    // 첫 페이지: 최신순 N개
    List<ChatMessageEntity> findTop50ByRoomIdOrderByIdDesc(String roomId);
}
