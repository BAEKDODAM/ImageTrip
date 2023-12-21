package com.ImageTrip.ScheduleLike.entity;

import com.ImageTrip.Schedule.entity.Schedule;
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
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;

    private long imageId;
    private long memberId;

}
