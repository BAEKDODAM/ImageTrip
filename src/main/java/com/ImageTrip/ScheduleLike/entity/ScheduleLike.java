package com.ImageTrip.ScheduleLike.entity;

import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class ScheduleLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scheduleLikeId;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
