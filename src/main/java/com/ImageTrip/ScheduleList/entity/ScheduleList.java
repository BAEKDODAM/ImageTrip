package com.ImageTrip.ScheduleList.entity;

import com.ImageTrip.Schedule.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public ScheduleList(String content, float lat, float lon, int priority, Schedule schedule) {
        this.content = content;
        this.lat = lat;
        this.lon = lon;
        this.priority = priority;
        this.schedule = schedule;
    }
}
