package com.ImageTrip.ScheduleLike.service;

import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.Schedule.repository.ScheduleRepository;
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
    private final LikeRepository likeRepository;
    private final ScheduleRepository scheduleRepository;

    public LikeService(LikeRepository likeRepository, ScheduleRepository scheduleRepository) {
        this.likeRepository = likeRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public int scheduleLikeCnt(long scheduleId){
        return (int) likeRepository.findAllByScheduleScheduleId(scheduleId).stream().count();
    }

    public void createLike(long scheduleId, Member member){
        Schedule schedule = scheduleRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND));

        if(hasLiked(schedule.getScheduleId(), member.getMemberId())) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_LIKED);
        }
        ScheduleLike like = new ScheduleLike(schedule, member);
        likeRepository.save(like);
    }
    public void deleteLike(long scheduleId, long memberId){
        if(!hasLiked(scheduleId, memberId)) {
            throw new BusinessLogicException(ExceptionCode.LIKE_NOT_FOUND);
        }
        likeRepository.deleteByScheduleScheduleIdAndMemberMemberId(scheduleId, memberId);
    }
    public void deleteAllLikeByScheduleId(long scheduleId){
        likeRepository.deleteAllByScheduleScheduleId(scheduleId);
    }

    public boolean hasLiked(long scheduleId, long memberId){
        return likeRepository.findByScheduleScheduleIdAndMemberMemberId(scheduleId, memberId).isPresent();
    }

    public List<ScheduleDto.ListResponse> findLikeScheduleByPage(long cursor, Pageable pageable, long memberId){
        List<Schedule> schedules = getLikedSchedules(cursor, pageable,memberId)
                .stream().map(scheduleLike -> scheduleLike.getSchedule()).collect(Collectors.toList());
        return schedules.stream().map(schedule -> {
            int likeCnt = scheduleLikeCnt(schedule.getScheduleId());
            boolean liked = hasLiked(schedule.getScheduleId(), memberId);
            return ScheduleDto.ListResponse.from(schedule, likeCnt, liked);
        }).collect(Collectors.toList());
    }

    public List<ScheduleLike> getLikedSchedules(Long cursor, Pageable pageable, long memberId) {
        return cursor.equals(0L)
                ? likeRepository.findByMemberMemberIdOrderByScheduleLikeIdDesc(memberId, pageable)
                : likeRepository.findByMemberMemberIdAndScheduleLikeIdLessThanOrderByScheduleLikeIdDesc(memberId, cursor, pageable);
    }
}
