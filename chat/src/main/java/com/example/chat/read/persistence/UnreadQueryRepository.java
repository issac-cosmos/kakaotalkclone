package com.example.chat.read.persistence;

import com.example.chat.domain.RoomParticipant;
import com.example.chat.domain.RoomParticipantId;
import com.example.chat.read.dtos.RoomUnreadCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnreadQueryRepository extends JpaRepository<RoomParticipant, RoomParticipantId> {
    @Query("""
        select new com.example.chat.read.dtos.RoomUnreadCountDto(
            rp.roomId,
            count(cm)
        )
        from RoomParticipant rp
        left join ChatMessageEntity cm
               on cm.roomId = rp.roomId
              and cm.id > rp.lastReadMessageId
        where rp.userId = :userId
        group by rp.roomId
    """)
    List<RoomUnreadCountDto> findUnreadCounts(@Param("userId") String userId);
}
