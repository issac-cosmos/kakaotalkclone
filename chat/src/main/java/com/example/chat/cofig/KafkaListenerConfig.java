package com.example.chat.cofig;

import com.example.chat.dtos.ChatMessageEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
public class KafkaListenerConfig {
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ChatMessageEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, ChatMessageEvent> consumerFactory
    ) {
        var f = new ConcurrentKafkaListenerContainerFactory<String, ChatMessageEvent>();
        f.setConsumerFactory(consumerFactory);
        f.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        f.setConcurrency(3); // 파티션 수 이하
        return f;
    }
}
