package com.example.user.repository;

import com.example.user.domain.Friendship;
import com.example.user.domain.FriendshipStatus;
import com.example.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository <Friendship,Long>{
    // 특정 사용자(user)가 보낸 특정 대상(friend)과의 관계 조회
    Optional<Friendship> findByUserAndFriend(User user, User friend);

    // 특정 사용자의 상태별 친구 목록 조회
    List<Friendship> findAllByUserAndStatus(User user, FriendshipStatus status);

    // 친구가 나에게 요청한 관계(역방향)
    Optional<Friendship> findByFriendAndUser(User friend, User user);
}
