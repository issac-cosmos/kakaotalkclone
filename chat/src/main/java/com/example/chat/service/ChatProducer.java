package com.example.chat.service;

import com.example.chat.dtos.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatProducer {
    private final KafkaTemplate<String, ChatMessageEvent> kafkaTemplate;

    @Value("${chat.topic.message}")
    private String topic;

    public void send(ChatMessageEvent event) {
        kafkaTemplate.send(topic, event.roomId(), event);
    }
}
