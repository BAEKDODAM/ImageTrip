package com.ImageTrip.Schedule.entity;

import com.ImageTrip.ScheduleLike.entity.ScheduleLike;
import com.ImageTrip.ScheduleList.entity.ScheduleList;
import com.ImageTrip.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scheduleId;

    @Column(nullable = false)
    private String title;

    private Boolean share;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;

    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "schedule")
    @JsonManagedReference
    private List<ScheduleList> scheduleLists;

    @OneToMany(mappedBy = "schedule")
    @JsonManagedReference
    private List<ScheduleLike> scheduleLikes;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    public Schedule(String title, Boolean share, LocalDate startDate, LocalDate endDate, Member member) {
        this.title = title;
        this.share = share;
        this.startDate = startDate;
        this.endDate = endDate;
        this.member = member;
    }
}
