package com.example.chat.kafka;

import com.example.chat.domain.ChatMessageEntity;
import com.example.chat.dtos.ChatMessageEvent;
import com.example.chat.dtos.RoomBadgeEvent;
import com.example.chat.repository.ChatMessageRepository;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.repository.RoomParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatConsumer {
    private final ChatMessageRepository messageRepo;
    private final ChatRoomRepository roomRepo;
    private final RoomParticipantRepository participantRepo;
    private final SimpMessagingTemplate messagingTemplate;
    @KafkaListener(topics = "${chat.topic.message}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(ChatMessageEvent event, Acknowledgment ack) {
        ChatMessageEntity saved = null;

        if (!messageRepo.existsByRoomIdAndMessageId(event.roomId(), event.messageId())) {
            saved = messageRepo.save(ChatMessageEntity.of(
                    event.roomId(), event.messageId(), event.senderId(), event.content()
            ));
            final ChatMessageEntity finalSaved = saved;
            // room last_message 갱신
            roomRepo.findById(event.roomId()).ifPresent(room -> {
                room.touchLastMessage(finalSaved.getId(), finalSaved.getCreatedAt());
                roomRepo.save(room);
            });

            // 방 메시지 브로드캐스트(현재 채팅방 보고 있는 사람들)
            messagingTemplate.convertAndSend("/sub/room/" + event.roomId(), event);
        }

        // ✅ 배지 이벤트는 “신규 저장된 경우”에만
        if (saved != null) {
            List<String> userIds =
                    participantRepo.findUserIdsByRoomId(event.roomId());

            RoomBadgeEvent badge = new RoomBadgeEvent(
                    event.roomId(),
                    1, // 상대방 unread +1
                    saved.getId(),
                    saved.getContent(),
                    System.currentTimeMillis()
            );

            for (String uid : userIds) {
                if (uid.equals(event.senderId())) continue; // 보낸 사람은 unread 증가 X
                messagingTemplate.convertAndSend("/sub/user/" + uid + "/rooms", badge);
            }
        }

        // ✅ 중복이든 아니든 여기서 커밋 (필수)
        ack.acknowledge();
    }
}
