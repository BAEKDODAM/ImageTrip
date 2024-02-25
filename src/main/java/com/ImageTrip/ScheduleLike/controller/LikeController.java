package com.ImageTrip.ScheduleLike.controller;

import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.Schedule.service.ScheduleService;
import com.ImageTrip.ScheduleLike.service.LikeService;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/like")
public class LikeController {
    private final MemberService memberService;
    private final ScheduleService scheduleService;
    private final LikeService likeService;
    private static final int PAGE_DEFAULT_SIZE = 10;


    public LikeController(MemberService memberService, ScheduleService scheduleService, LikeService likeService) {
        this.memberService = memberService;
        this.scheduleService = scheduleService;
        this.likeService = likeService;
    }

    @ApiOperation(value = "좋아요")
    @PostMapping("/{scheduleId}")
    public ResponseEntity postLike(@PathVariable("scheduleId") int scheduleId,
                                   @RequestHeader(value = "Authorization") String token) {
        Member member = memberService.findMemberByToken(token);
        Schedule schedule = scheduleService.findVerifiedSchedule(scheduleId);
        likeService.createLike(schedule, member);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "좋아요 취소")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity deleteLike(@PathVariable("scheduleId") int scheduleId,
                                     @RequestHeader(value = "Authorization") String token) {
        long memberId = memberService.getMemberIdFromToken(token);
        likeService.deleteLike(scheduleId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "좋아요 한 일정 조회")
    @GetMapping()
    public ResponseEntity getLikeSchedules(@RequestParam long cursor,
                                           @RequestHeader(value = "Authorization") String token) {
        long memberId = memberService.getMemberIdFromToken(token);
        List<ScheduleDto.ListResponse> likeSchedules = likeService.findLikeScheduleByPage(cursor, PageRequest.of(0, PAGE_DEFAULT_SIZE), memberId);
        return new ResponseEntity<>(likeSchedules, HttpStatus.OK);
    }
}