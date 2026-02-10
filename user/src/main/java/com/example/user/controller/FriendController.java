package com.example.user.controller;

import com.example.user.domain.User;
import com.example.user.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friendship")
public class FriendController {
    final FriendshipService friendshipService;

    //친구요청 보내기
    @PostMapping("/requested")
    public ResponseEntity<?> friendshipRequest(@RequestParam Long myId, @RequestParam Long targetId){
        friendshipService.friendshipRequest(myId,targetId);
        return ResponseEntity.ok("me => friend / Friendship requested!");
    }
    //친구요청 수락
    @PatchMapping("/{targetId}/accepted")
    public ResponseEntity<?> friendshipAccept(@RequestParam Long myId, @PathVariable Long targetId){
        friendshipService.friendshipAccept(myId,targetId);
        return ResponseEntity.ok("me => friend / Friendship accepted!");
    }
    //친구요청 거절
    @PatchMapping("/{targetId}/rejected")
    public ResponseEntity<?> friendshipReject(@RequestParam Long myId, @PathVariable Long targetId){
        friendshipService.friendshipReject(myId,targetId);
        return ResponseEntity.ok("me => friend / Friendship rejected");
    }
}
