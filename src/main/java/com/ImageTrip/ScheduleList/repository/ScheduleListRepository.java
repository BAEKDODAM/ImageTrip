package com.ImageTrip.ScheduleList.repository;

import com.ImageTrip.ScheduleList.entity.ScheduleList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleListRepository extends JpaRepository<ScheduleList, Long> {
    List<ScheduleList> findAllByScheduleScheduleId(long scheduleId);

    void deleteAllByScheduleScheduleId(long ScheduleId);
}
