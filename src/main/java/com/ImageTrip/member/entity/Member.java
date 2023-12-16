package com.ImageTrip.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = false, updatable = true, unique = true)
    private String name;

    @Column(nullable = false, updatable = true, unique = false)
    private String password;

    @Column(nullable = false, updatable = false, unique = false)
    private LocalDateTime createdAt;

    //private String nickName;

    //private String image;

    //Schedule, Image, Like, MemberFile 연관관계 매핑 필요

    public Member(String email, String name, String password){
        this.email = email;
        this.name = name;
        this.password = password;
    }

}
