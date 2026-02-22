package com.example.chat.controller;

import com.example.chat.dtos.RoomListItem;
import com.example.chat.repository.RoomListQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomListQueryRepository roomListQueryRepository;

    @GetMapping
    public List<RoomListItem> list(@RequestHeader("X-User-Id") String userId) {
        return roomListQueryRepository.findRoomsWithUnread(userId);
    }
}