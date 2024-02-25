package com.ImageTrip.ScheduleLike.service;

import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.ScheduleLike.entity.ScheduleLike;
import com.ImageTrip.ScheduleLike.repository.LikeRepository;
import com.ImageTrip.exception.BusinessLogicException;
import com.ImageTrip.exception.ExceptionCode;
import com.ImageTrip.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LikeService {
    private final LikeRepository repository;

    public LikeService(LikeRepository repository) {
        this.repository = repository;
    }

    public int scheduleLikeCnt(long scheduleId){
        return (int) repository.findAllByScheduleScheduleId(scheduleId).stream().count();
    }

    public void createLike(Schedule schedule, Member member){
        if(findLike(schedule.getScheduleId(), member.getMemberId())) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_LIKED);
        }

        ScheduleLike like = new ScheduleLike();
        like.setSchedule(schedule);
        like.setMember(member);
        repository.save(like);
    }
    public void deleteLike(long scheduleId, long memberId){
        if(!findLike(scheduleId, memberId)) {
            throw new BusinessLogicException(ExceptionCode.LIKE_NOT_FOUND);
        }
        repository.deleteByScheduleScheduleIdAndMemberMemberId(scheduleId, memberId);
    }
    public void deleteAllLikeByScheduleId(long scheduleId){
        repository.deleteAllByScheduleScheduleId(scheduleId);
    }

    public boolean findLike(long scheduleId, long memberId){
        return repository.findByScheduleScheduleIdAndMemberMemberId(scheduleId, memberId).isPresent();
    }

    public List<ScheduleDto.ListResponse> findLikeScheduleByPage(long cursor, Pageable pageable, long memberId){
        List<Schedule> schedules = getLikedSchedules(cursor, pageable,memberId)
                .stream().map(scheduleLike -> scheduleLike.getSchedule()).collect(Collectors.toList());
        return schedules.stream().map(schedule -> {
            int likeCnt = scheduleLikeCnt(schedule.getScheduleId());
            boolean liked = findLike(schedule.getScheduleId(), memberId);
            return ScheduleDto.ListResponse.from(schedule, likeCnt, liked);
        }).collect(Collectors.toList());
    }

    public List<ScheduleLike> getLikedSchedules(Long cursor, Pageable pageable, long memberId) {
        return cursor.equals(0L)
                ? repository.findByMemberMemberIdOrderByScheduleLikeIdDesc(pageable)
                : repository.findByMemberMemberIdAndScheduleLikeIdLessThanOrderByScheduleLikeIdDesc(memberId, cursor, pageable);
    }
}
