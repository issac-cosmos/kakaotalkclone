package com.example.chat.read;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatReadProducer {

    private final KafkaTemplate<String, ChatReadEvent> kafkaTemplate;

    @Value("${chat.topic.read}")
    private String topic;

    public void publish(ChatReadEvent event) {
        kafkaTemplate.send(topic, event.roomId(), event);
    }
}