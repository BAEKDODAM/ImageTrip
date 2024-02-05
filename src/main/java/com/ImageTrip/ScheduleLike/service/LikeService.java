package com.ImageTrip.ScheduleLike.service;

import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.ScheduleLike.entity.ScheduleLike;
import com.ImageTrip.ScheduleLike.repository.LikeRepository;
import com.ImageTrip.exception.BusinessLogicException;
import com.ImageTrip.exception.ExceptionCode;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.repository.MemberRepository;
import com.ImageTrip.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LikeService {
    private final LikeRepository repository;
    private final MemberService memberService;


    public LikeService(LikeRepository repository, MemberService memberService) {
        this.repository = repository;
        this.memberService = memberService;
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

}
