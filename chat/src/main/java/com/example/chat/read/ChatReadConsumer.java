package com.example.chat.read;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatReadConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "${chat.topic.read}")
    public void consume(ChatReadEvent event) {
        // 읽음 이벤트 브로드캐스트
        messagingTemplate.convertAndSend("/sub/room/" + event.roomId() + "/read", event);
    }
}