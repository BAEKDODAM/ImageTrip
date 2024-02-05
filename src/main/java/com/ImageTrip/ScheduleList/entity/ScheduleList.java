package com.ImageTrip.ScheduleList.entity;

import com.ImageTrip.Schedule.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ScheduleList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scheduleListId;
    @Column(nullable = false)
    private String content;
    private float lat;
    private float lon;
    @Column(nullable = false)
    private int priority;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;
}
