package com.example.chat.cofig;

import com.example.chat.dtos.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "${chat.topic.message}")
    public void consume(ChatMessageEvent event) {

        messagingTemplate.convertAndSend(
                "/sub/room/" + event.roomId(),
                event
        );
    }
}
