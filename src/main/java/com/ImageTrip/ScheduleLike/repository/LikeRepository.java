package com.ImageTrip.ScheduleLike.repository;

import com.ImageTrip.ScheduleLike.entity.ScheduleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<ScheduleLike, Long> {
    List<ScheduleLike> findAllByScheduleScheduleId(long scheduleId);
    //List<ScheduleLike> findAllByImageImageId(long imageId);
    Optional<ScheduleLike> findByScheduleScheduleIdAndMemberMemberId(long scheduleId, long memberId);

    void deleteAllByScheduleScheduleId(long ScheduleId);

    void deleteByScheduleScheduleIdAndMemberMemberId(long scheduleId, long memberId);
}
