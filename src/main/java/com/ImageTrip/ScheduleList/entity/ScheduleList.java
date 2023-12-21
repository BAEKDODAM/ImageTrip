package com.ImageTrip.ScheduleList.entity;

import com.ImageTrip.Schedule.entity.Schedule;

import javax.persistence.*;

@Entity
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
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;
}
