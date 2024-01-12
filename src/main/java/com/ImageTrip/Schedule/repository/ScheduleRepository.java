package com.ImageTrip.Schedule.repository;

import com.ImageTrip.Schedule.entity.Schedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByShareTrueOrderByScheduleIdDesc(Pageable pageable);
    List<Schedule> findByShareTrueAndScheduleIdLessThanOrderByScheduleIdDesc(long scheduleId, Pageable pageable);

    List<Schedule> findByMemberMemberIdOrderByScheduleIdDesc(Long memberId, Pageable page);
    List<Schedule> findByMemberMemberIdAndScheduleIdLessThanOrderByScheduleIdDesc(long memberId, long scheduleId, Pageable page);

    List<Schedule> findByShareTrueAndTitleContainingOrderByScheduleIdDesc(String search, Pageable pageable);
    List<Schedule> findByShareTrueAndTitleContainingAndScheduleIdLessThanOrderByScheduleIdDesc(String search, long scheduleId, Pageable pageable);
}
