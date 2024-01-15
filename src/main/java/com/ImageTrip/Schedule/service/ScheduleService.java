package com.ImageTrip.Schedule.service;

import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.Schedule.repository.ScheduleRepository;
import com.ImageTrip.ScheduleLike.service.LikeService;
import com.ImageTrip.ScheduleList.dto.ScheduleListDto;
import com.ImageTrip.ScheduleList.entity.ScheduleList;
import com.ImageTrip.ScheduleList.service.ScheduleListService;
import com.ImageTrip.exception.BusinessLogicException;
import com.ImageTrip.exception.ExceptionCode;
import com.ImageTrip.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleListService scheduleListService;
    private final LikeService likeService;

    public ScheduleService(ScheduleRepository scheduleRepository, ScheduleListService scheduleListService, LikeService likeService) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleListService = scheduleListService;
        this.likeService = likeService;
    }

    public ScheduleDto.Response createSchedule(long memberId, Schedule postSchedule, List<ScheduleListDto.Post> scheduleLists) {
        //Member member = memberService.getMemberByMemberId(memberId)
        Member member = new Member("email@email.com","name","password");
        member.setMemberId(1L);
        postSchedule.setMember(member);
        Schedule saveSchedule = scheduleRepository.save(postSchedule);
        List<ScheduleList> SaveScheduleLists = scheduleListService.saveScheduleLists(scheduleLists, saveSchedule);
        Schedule findSchedule = findVerifiedSchedule(saveSchedule.getScheduleId());
        ScheduleDto.Response response = ScheduleDto.Response.from(findSchedule, member, SaveScheduleLists, 0);

        return response;
    }

    public ScheduleDto.Response updateSchedule(long memberId, long scheduleId, Schedule postSchedule, List<ScheduleListDto.Post> scheduleLists) {
        //Member member = memberService.getMemberByMemberId(memberId)
        Member member = new Member();
        member.setMemberId(1L);
        Schedule findSchedule = findVerifiedSchedule(scheduleId);
        validateWriter(memberId, findSchedule);

        Optional.ofNullable(postSchedule.getTitle())
                .ifPresent(title -> findSchedule.setTitle(title));
        Optional.ofNullable(postSchedule.getStartDate())
                .ifPresent(startDate -> findSchedule.setStartDate(startDate));
        Optional.ofNullable(postSchedule.getEndDate())
                .ifPresent(endDate -> findSchedule.setEndDate(endDate));
        Optional.ofNullable(postSchedule.isShare())
                .ifPresent(isShare -> findSchedule.setShare(isShare));

        List<ScheduleList> saveScheduleLists = scheduleListService.changeScheduleLists(scheduleId, scheduleLists, findSchedule);
        Schedule saveSchedule = scheduleRepository.save(findSchedule);

        int likeCnt = likeService.scheduleLikeCnt(scheduleId);

        return ScheduleDto.Response.from(saveSchedule, member, saveScheduleLists, likeCnt);
    }

    public void deleteSchedule(long memberId, long scheduleId){
        Schedule findSchedule = findVerifiedSchedule(scheduleId);
        validateWriter(memberId, findSchedule);
        scheduleListService.deleteScheduleListsByScheduleId(scheduleId);
        likeService.deleteLikeByScheduleId(scheduleId);
        scheduleRepository.deleteById(scheduleId);
    }

    public List<ScheduleDto.ListResponse> findMyScheduleByPage(long cursor, long memberId, Pageable pageable){
        //Member member = memberService.getMemberByMemberId(memberId)
        Member member = new Member();
        List<Schedule> schedules = getMySchedules(cursor, pageable, memberId)
                .stream().collect(Collectors.toList());

        return schedules.stream().map(schedule -> {
            int likeCnt = likeService.scheduleLikeCnt(schedule.getScheduleId());
            return ScheduleDto.ListResponse.from(schedule, member, likeCnt);
        }).collect(Collectors.toList());
    }
    // 페이징 처리를 위한 메서드
    public List<Schedule> getMySchedules(Long cursor, Pageable page, long memberId){
        return cursor.equals(0L)
                ? scheduleRepository.findByMemberMemberIdOrderByIdDesc(memberId, page)
                : scheduleRepository.findByMemberMemberIdAndIdLessThanOrderByIdDesc(memberId,cursor, page);
    }

    public List<ScheduleDto.ListResponse> findSharedSchedulesByPage(long cursor, Pageable pageable){
        List<Schedule> schedules = getSharedSchedules(cursor, pageable)
                .stream().collect(Collectors.toList());
        return schedules.stream().map(schedule -> {
            int likeCnt = likeService.scheduleLikeCnt(schedule.getScheduleId());
            return ScheduleDto.ListResponse.from(schedule, likeCnt);
        }).collect(Collectors.toList());
    }

    public List<Schedule> getSharedSchedules(Long cursor, Pageable pageable){
        return cursor.equals(0L)
                ? scheduleRepository.findByShareTrueOrderByIdDesc(pageable)
                : scheduleRepository.findByShareTrueAndIdLessThanOrderByIdDesc(cursor, pageable);
    }

    public ScheduleDto.Response getScheduleDetail(long scheduleId) {
        Schedule findSchedule = findVerifiedSchedule(scheduleId);
        int likeCnt = likeService.scheduleLikeCnt(scheduleId);
        return ScheduleDto.Response.from(findSchedule, likeCnt);
    }

    public List<ScheduleDto.ListResponse> findSearchSchedule(Long cursor, Pageable pageable, String search) {
        List<Schedule> schedules = getSearchSchedule(cursor, pageable, search)
                .stream().collect(Collectors.toList());
        return schedules.stream().map(schedule -> {
            int likeCnt = likeService.scheduleLikeCnt(schedule.getScheduleId());
            return ScheduleDto.ListResponse.from(schedule, likeCnt);
        }).collect(Collectors.toList());

    }
    public List<Schedule> getSearchSchedule(Long cursor, Pageable pageable, String search) {
        return cursor.equals(0L)
                ? scheduleRepository.findByShardTrueAndSearchContainingOrderByIdDesc(search, pageable)
                : scheduleRepository.findByShareTrueAndSearchContainingAndIdLessThanOrderByIdDesc(search, cursor, pageable);
    }

    public void validateWriter(long memberId, Schedule schedule) {
        if (schedule.getMember().getMemberId() != memberId) throw new BusinessLogicException(ExceptionCode.UNMATCHED_WRITER);
    }
    public Schedule findVerifiedSchedule(long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND));
    }
}
