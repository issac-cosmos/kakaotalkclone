package com.example.user.domain;

import com.example.user.dtos.UserProfileDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 8)
    private String userId;
    private String password;

//    @Column(nullable = false)
    private String nickName;
    private String phoneNumber;
    private String birth;
    private String profileImage; //기본이미지 연결할꺼임
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true, length = 50)
    private String searchId;
    private String statusMessage;
    private LocalDateTime createAt;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN=DelYN.N;
    //친구상태 설정
    // 나로부터 시작된 친구 관계들 (요청한 쪽)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friendship> friendships = new ArrayList<>();

    // 나에게 요청된 친구 관계들 (요청받은 쪽)
    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friendship> friendOf = new ArrayList<>();

    public void addFriendship(Friendship friendship) {
        friendships.add(friendship);
    }

    private User(String birth, String email, String searchId) {
        this.birth = birth;
        this.email = email;
        this.searchId = searchId;
    }

    public static User create(String birth, String email, String searchId) {
        return new User(birth, email, searchId);
    }
    public UserProfileDto profileFromEntity(){
        return UserProfileDto.builder().id(this.id).NickName(this.nickName).profileImage(this.profileImage).build();
    }

    public void toEntity(String id, String pw){
        this.userId = id;
        this.password = pw;
    }
}
