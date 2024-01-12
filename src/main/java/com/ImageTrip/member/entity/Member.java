package com.ImageTrip.member.entity;

import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.ScheduleLike.entity.ScheduleLike;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

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
    private LocalDateTime createdAt = LocalDateTime.now();

    //private String nickName;

    @Column(nullable = true, updatable = true, unique = false)
    private String image = "example_image_link";

    //Schedule, Image, Like, MemberFile 연관관계 매핑 필요
    @OneToMany(mappedBy = "member")
    private List<Schedule> schedules;


    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();


    public Member(String email, String name, String password){
        this.email = email;
        this.name = name;
        this.password = password;
    }

}
