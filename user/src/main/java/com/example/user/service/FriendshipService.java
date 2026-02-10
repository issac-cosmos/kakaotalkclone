package com.example.user.service;

import com.example.user.domain.Friendship;
import com.example.user.domain.FriendshipStatus;
import com.example.user.domain.User;
import com.example.user.repository.FriendshipRepository;
import com.example.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendshipService {
    final FriendshipRepository friendshipRepository;
    final UserRepository userRepository;

    public void friendshipRequest(Long myId, Long targetId){
        User me = userRepository.findById(myId).orElseThrow(()->new EntityNotFoundException("FriendshipService.friendshipRequest : not found user"));
        User target = userRepository.findById(targetId).orElseThrow(()->new EntityNotFoundException("UserService.friendshipRequest : not found friend"));
        Friendship friendship = Friendship.builder()
                .user(me)
                .friend(target)
                .status(FriendshipStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        friendshipRepository.save(friendship);
    }
    public void friendshipAccept(Long myId, Long targetId){
        User me = userRepository.findById(myId).orElseThrow(()->new EntityNotFoundException("FriendshipService.friendshipRequest : not found user"));
        User target = userRepository.findById(targetId).orElseThrow(()->new EntityNotFoundException("FriendshipService.friendshipRequest : not found friend"));
        Friendship friendship = friendshipRepository.findByUserAndFriend(me,target).orElseThrow(()->new EntityNotFoundException("UserService.friendshipRequest : not found friend"));

        friendshipRepository.save(friendship);
    }

    public void friendshipReject(Long myId, Long targetId){
        User me = userRepository.findById(myId).orElseThrow(()->new EntityNotFoundException("FriendshipService.friendshipRequest : not found user"));
        User target = userRepository.findById(targetId).orElseThrow(()->new EntityNotFoundException("FriendshipService.friendshipRequest : not found friend"));
        Friendship friendship = Friendship.builder()
                .user(me)
                .friend(target)
                .status(FriendshipStatus.REJECTED)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
