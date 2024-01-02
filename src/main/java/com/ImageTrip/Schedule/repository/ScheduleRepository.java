package com.ImageTrip.Schedule.repository;

import com.ImageTrip.Schedule.entity.Schedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByShareTrueOrderByIdDesc(Pageable pageable);
    List<Schedule> findByShareTrueAndIdLessThanOrderByIdDesc(long id, Pageable pageable);

    List<Schedule> findByMemberMemberIdOrderByIdDesc(Long memberId, Pageable page);
    List<Schedule> findByMemberMemberIdAndIdLessThanOrderByIdDesc(long memberId, long id, Pageable page);

    List<Schedule> findByShardTrueAndSearchContainingOrderByIdDesc(String search, Pageable pageable);
    List<Schedule> findByShareTrueAndSearchContainingAndIdLessThanOrderByIdDesc(String search, long id, Pageable pageable);
}
