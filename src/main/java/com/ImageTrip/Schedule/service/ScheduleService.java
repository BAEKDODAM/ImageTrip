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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleListService scheduleListService;
    private final LikeService likeService;

    public ScheduleService(ScheduleRepository scheduleRepository, ScheduleListService scheduleListService, LikeService likeService) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleListService = scheduleListService;
        this.likeService = likeService;
    }

    public ScheduleDto.Response createSchedule(Schedule postSchedule, List<ScheduleListDto.Post> scheduleLists, Member member) {
        postSchedule.setMember(member);
        Schedule saveSchedule = scheduleRepository.save(postSchedule);
        List<ScheduleList> SaveScheduleLists = scheduleListService.saveScheduleLists(scheduleLists, saveSchedule);
        Schedule findSchedule = findVerifiedSchedule(saveSchedule.getScheduleId());
        ScheduleDto.Response response = ScheduleDto.Response.from(findSchedule, member, SaveScheduleLists, 0);

        return response;
    }

    public ScheduleDto.Response updateSchedule(long memberId, long scheduleId, Schedule postSchedule, List<ScheduleListDto.Post> scheduleLists) {
        Schedule findSchedule = findVerifiedSchedule(scheduleId);
        validateWriter(memberId, findSchedule);

        Optional.ofNullable(postSchedule.getTitle())
                .ifPresent(title -> findSchedule.setTitle(title));
        Optional.ofNullable(postSchedule.getStartDate())
                .ifPresent(startDate -> findSchedule.setStartDate(startDate));
        Optional.ofNullable(postSchedule.getEndDate())
                .ifPresent(endDate -> findSchedule.setEndDate(endDate));
        Optional.ofNullable(postSchedule.getShare())
                .ifPresent(share -> findSchedule.setShare(share));
        Schedule saveSchedule = scheduleRepository.save(findSchedule);

        Optional.ofNullable(scheduleLists)
                .ifPresent(schedules -> scheduleListService.changeScheduleLists(scheduleId, schedules, findSchedule));

        int likeCnt = likeService.scheduleLikeCnt(scheduleId);
        boolean liked = likeService.findLike(scheduleId, memberId);

        List<ScheduleList> returnScheduleList = scheduleListService.findSchduleListByScheduleId(scheduleId);

        return ScheduleDto.Response.from(saveSchedule, returnScheduleList, likeCnt, liked);
    }

    public void deleteSchedule(long memberId, long scheduleId){
        Schedule findSchedule = findVerifiedSchedule(scheduleId);
        validateWriter(memberId, findSchedule);
        scheduleListService.deleteScheduleListsByScheduleId(scheduleId);
        likeService.deleteAllLikeByScheduleId(scheduleId);
        scheduleRepository.deleteById(scheduleId);
    }

    public List<ScheduleDto.ListResponse> findMyScheduleByPage(long cursor, Member member, Pageable pageable){
        //Member member = memberService.getMemberByMemberId(memberId)
        List<Schedule> schedules = getMySchedules(cursor, pageable, member.getMemberId())
                .stream().collect(Collectors.toList());

        return schedules.stream().map(schedule -> {
            int likeCnt = likeService.scheduleLikeCnt(schedule.getScheduleId());
            boolean liked = likeService.findLike(schedule.getScheduleId(), member.getMemberId());
            return ScheduleDto.ListResponse.from(schedule, member, likeCnt, liked);
        }).collect(Collectors.toList());
    }
    // 페이징 처리를 위한 메서드
    public List<Schedule> getMySchedules(Long cursor, Pageable page, long memberId){
        return cursor.equals(0L)
                ? scheduleRepository.findByMemberMemberIdOrderByScheduleIdDesc(memberId, page)
                : scheduleRepository.findByMemberMemberIdAndScheduleIdLessThanOrderByScheduleIdDesc(memberId,cursor, page);
    }

    public List<ScheduleDto.ListResponse> findSharedSchedulesByPage(long cursor, Pageable pageable, long memberId){
        List<Schedule> schedules = getSharedSchedules(cursor, pageable)
                .stream().collect(Collectors.toList());
        return schedules.stream().map(schedule -> {
            int likeCnt = likeService.scheduleLikeCnt(schedule.getScheduleId());
            boolean liked = likeService.findLike(schedule.getScheduleId(), memberId);
            return ScheduleDto.ListResponse.from(schedule, likeCnt, liked);
        }).collect(Collectors.toList());
    }

    public List<Schedule> getSharedSchedules(Long cursor, Pageable pageable){
        return cursor.equals(0L)
                ? scheduleRepository.findByShareTrueOrderByScheduleIdDesc(pageable)
                : scheduleRepository.findByShareTrueAndScheduleIdLessThanOrderByScheduleIdDesc(cursor, pageable);
    }

    @Transactional
    public ScheduleDto.Response getScheduleDetail(long scheduleId, long memberId) {
        Schedule findSchedule = findVerifiedSchedule(scheduleId);
        int likeCnt = likeService.scheduleLikeCnt(scheduleId);
        boolean liked = likeService.findLike(scheduleId, memberId);
        List<ScheduleList> scheduleLists = scheduleListService.findSchduleListByScheduleId(scheduleId);
        return ScheduleDto.Response.from(findSchedule, scheduleLists, likeCnt, liked);
    }

    public List<ScheduleDto.ListResponse> findSearchSchedule(Long cursor, Pageable pageable, String search, long memberId) {
        List<Schedule> schedules = getSearchSchedule(cursor, pageable, search)
                .stream().collect(Collectors.toList());
        return schedules.stream().map(schedule -> {
            int likeCnt = likeService.scheduleLikeCnt(schedule.getScheduleId());
            boolean liked = likeService.findLike(schedule.getScheduleId(), memberId);
            return ScheduleDto.ListResponse.from(schedule, likeCnt, liked);
        }).collect(Collectors.toList());

    }
    public List<Schedule> getSearchSchedule(Long cursor, Pageable pageable, String search) {
        return cursor.equals(0L)
                ? scheduleRepository.findByShareTrueAndTitleContainingOrderByScheduleIdDesc(search, pageable)
                : scheduleRepository.findByShareTrueAndTitleContainingAndScheduleIdLessThanOrderByScheduleIdDesc(search, cursor, pageable);
    }

    public void validateWriter(long memberId, Schedule schedule) {
        if (schedule.getMember().getMemberId() != memberId) throw new BusinessLogicException(ExceptionCode.UNMATCHED_WRITER);
    }
    public Schedule findVerifiedSchedule(long scheduleId) {
        return scheduleRepository.findByScheduleId(scheduleId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND));
    }
}
