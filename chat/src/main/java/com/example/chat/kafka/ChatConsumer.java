package com.example.chat.kafka;

import com.example.chat.domain.ChatMessageEntity;
import com.example.chat.dtos.ChatMessageEvent;
import com.example.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChatConsumer {
    private final ChatMessageRepository repo;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "${chat.topic.message}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(ChatMessageEvent event, Acknowledgment ack) {
        // 멱등: 이미 저장된 messageId면 스킵(정책)
        if (!repo.existsByRoomIdAndMessageId(event.roomId(), event.messageId())) {
            repo.save(ChatMessageEntity.of(event.roomId(), event.messageId(), event.senderId(), event.content()));

            // 저장 성공 후 브로드캐스트 (멀티 인스턴스 대비)
            messagingTemplate.convertAndSend("/sub/room/" + event.roomId(), event);
            // 마지막: 커밋
            ack.acknowledge();
        }
    }
}
