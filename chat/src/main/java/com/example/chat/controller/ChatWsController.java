package com.example.chat.controller;

import com.example.chat.dtos.ChatMessageEvent;
import com.example.chat.service.ChatProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final ChatProducer producer;

    @MessageMapping("/chat.send")
    public void send(ChatMessageEvent event) {
        producer.send(event);
    }
}