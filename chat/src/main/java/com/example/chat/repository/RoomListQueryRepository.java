package com.example.chat.repository;

import com.example.chat.dtos.RoomListItem;

import java.util.List;

public interface RoomListQueryRepository {
    List<RoomListItem> findRoomsWithUnread(String userId);
}