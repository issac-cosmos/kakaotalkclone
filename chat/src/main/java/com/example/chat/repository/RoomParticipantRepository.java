package com.example.chat.repository;

import com.example.chat.domain.RoomParticipant;
import com.example.chat.domain.RoomParticipantId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, RoomParticipantId> {

    @Modifying
    @Transactional
    @Query("""
        update RoomParticipant rp
           set rp.lastReadMessageId = case when rp.lastReadMessageId < :newId then :newId else rp.lastReadMessageId end,
               rp.lastReadAt = CURRENT_TIMESTAMP
         where rp.roomId = :roomId
           and rp.userId = :userId
    """)
    int advanceLastRead(@Param("roomId") String  roomId,
                        @Param("userId") String userId,
                        @Param("newId") long newId);
    //참가자 조회
    @Query("select rp.userId from RoomParticipant rp where rp.roomId = :roomId")
    List<String> findUserIdsByRoomId(@Param("roomId") String roomId);
}