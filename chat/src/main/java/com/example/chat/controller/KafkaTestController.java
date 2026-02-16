package com.example.chat.controller;

import com.example.chat.dtos.ChatMessageEvent;
import com.example.chat.service.ChatProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaTestController {
    private final ChatProducer producer;

    @PostMapping("/test/kafka")
    public void test(@RequestBody ChatMessageEvent event) {
        producer.send(event);
    }
}
