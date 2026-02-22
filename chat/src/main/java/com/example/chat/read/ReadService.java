package com.example.chat.read;

import com.example.chat.domain.RoomParticipant;
import com.example.chat.domain.RoomParticipantId;
import com.example.chat.read.dtos.RoomUnreadCountDto;
import com.example.chat.read.persistence.UnreadQueryRepository;
import com.example.chat.repository.RoomParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadService {

    private final RoomParticipantRepository repo;
    private final UnreadQueryRepository unRepo;
    private final ChatReadProducer producer;

    @Transactional
    public void updateLastRead(String roomId, String userId, long lastReadMessageId) {
        // 참가자 row 없으면 생성 (MVP)
        var id = new RoomParticipantId(roomId, userId);
        repo.findById(id).orElseGet(() -> repo.save(RoomParticipant.create(roomId, userId)));

        // 단조 증가 업데이트
        repo.advanceLastRead(roomId, userId, lastReadMessageId);

        // Kafka로 read 이벤트 발행 (멀티 인스턴스 브로드캐스트 보장)
        producer.publish(new ChatReadEvent(
                roomId,
                userId,
                lastReadMessageId,
                UUID.randomUUID().toString(),
                System.currentTimeMillis()
        ));
    }
    public List<RoomUnreadCountDto> getUnreadCounts(String userId) {
        return unRepo.findUnreadCounts(userId);
    }
}