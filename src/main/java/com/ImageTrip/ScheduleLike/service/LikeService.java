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
    private final MemberRepository memberRepository;

    public LikeService(LikeRepository repository, MemberService memberService, MemberRepository memberRepository) {
        this.repository = repository;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    public int scheduleLikeCnt(long scheduleId){
        return (int) repository.findAllByScheduleScheduleId(scheduleId).stream().count();
    }

    public void createLike(Schedule schedule, long memberId){
        if(findLike(schedule.getScheduleId(), memberId)) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_LIKED);
        }
        //Schedule schedule = scheduleService.findVerifiedSchedule(scheduleId);
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.UNMATCHED_WRITER));

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
    public void deleteLikeByImageId(long imageId){}

    public boolean findLike(long scheduleId, long memberId){
        return repository.findByScheduleScheduleIdAndMemberMemberId(scheduleId, memberId).isPresent();
    }

}
