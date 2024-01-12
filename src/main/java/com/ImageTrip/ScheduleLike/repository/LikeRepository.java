package com.ImageTrip.ScheduleLike.repository;

import com.ImageTrip.ScheduleLike.entity.ScheduleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<ScheduleLike, Long> {
    List<ScheduleLike> findAllByScheduleScheduleId(long scheduleId);
    //List<ScheduleLike> findAllByImageImageId(long imageId);

    void deleteAllByScheduleScheduleId(long ScheduleId);
}
