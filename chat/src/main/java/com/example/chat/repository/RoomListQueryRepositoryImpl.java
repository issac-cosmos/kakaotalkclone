package com.example.chat.repository;

import com.example.chat.dtos.RoomListItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomListQueryRepositoryImpl implements RoomListQueryRepository {

    private final EntityManager em;

    @Override
    public List<RoomListItem> findRoomsWithUnread(String userId) {
        String sql = """
            SELECT
              r.room_id AS roomId,
              r.title AS title,
              r.type AS type,
              r.last_message_id AS lastMessageId,
              lm.content AS lastMessageContent,
              r.last_message_at AS lastMessageAt,
              COALESCE(COUNT(um.id), 0) AS unreadCount
            FROM room_participant rp
            JOIN chat_room r ON r.room_id = rp.room_id
            LEFT JOIN chat_message lm ON lm.id = r.last_message_id
            LEFT JOIN chat_message um
                   ON um.room_id = rp.room_id
                  AND um.id > rp.last_read_message_id
            WHERE rp.user_id = :userId
            GROUP BY r.room_id, r.title, r.type, r.last_message_id, lm.content, r.last_message_at
            ORDER BY r.updated_at DESC
        """;

        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .getResultList();

        return rows.stream().map(r -> new RoomListItem(
                (String) r[0],
                (String) r[1],
                (String) r[2],
                r[3] == null ? null : ((Number) r[3]).longValue(),
                (String) r[4],
                r[5] == null ? null : ((java.sql.Timestamp) r[5]).toInstant(),
                ((Number) r[6]).longValue()
        )).toList();
    }
}