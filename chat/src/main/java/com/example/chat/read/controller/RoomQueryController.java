package com.example.chat.read.controller;

import com.example.chat.read.ReadService;
import com.example.chat.read.dtos.RoomUnreadCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomQueryController {

    private final ReadService readService;

    @GetMapping("/unread")
    public List<RoomUnreadCountDto> unread(
            @RequestHeader("X-User-Id") String userId
    ) {
        return readService.getUnreadCounts(userId);
    }
}