package com.example.chat.read;

import com.example.chat.dtos.ReadUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class ReadController {

    private final ReadService readService;

    @PostMapping("/{roomId}/read")
    public ResponseEntity<Void> read(
            @PathVariable String roomId,
            @RequestHeader("X-User-Id") String userId,
            @RequestBody @Valid ReadUpdateRequest request
    ) {
        readService.updateLastRead(roomId, userId, request.lastReadMessageId());
        return ResponseEntity.noContent().build();
    }
}