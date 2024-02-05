package com.ImageTrip.Schedule.controller;

import com.ImageTrip.Response.MultiResponseDto;
import com.ImageTrip.Response.PageInfo;
import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.Schedule.mapper.ScheduleMapper;
import com.ImageTrip.Schedule.service.ScheduleService;
import com.ImageTrip.ScheduleList.dto.ScheduleListDto;
import com.ImageTrip.ScheduleList.entity.ScheduleList;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ScheduleMapper mapper;
    private final MemberService memberService;
    private static final int PAGE_DEFAULT_SIZE = 10;

    public ScheduleController(ScheduleService scheduleService, ScheduleMapper mapper, MemberService memberService) {
        this.scheduleService = scheduleService;
        this.memberService = memberService;
        this.mapper = mapper;
    }

    @PostMapping
    @ApiOperation(value = "일정 생성")
    public ResponseEntity postSchedule(@Valid @RequestBody ScheduleDto.Post requestBody,
                                       @RequestHeader(value = "Authorization") String token) throws Exception, IOException {
        Member member = memberService.findMemberByToken(token);
        List<ScheduleListDto.Post> scheduleList = requestBody.getScheduleList();
        Schedule postSchedule = mapper.schedulePostDtoToSchedule(requestBody);
        ScheduleDto.Response response = scheduleService.createSchedule(postSchedule, scheduleList, member);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "일정 수정")
    @PatchMapping("/{scheduleId}")
    public ResponseEntity patchSchedule(@PathVariable("scheduleId") int scheduleId,
                                        @RequestBody ScheduleDto.Post requestBody,
                                        @RequestHeader(value = "Authorization") String token) {
        List<ScheduleListDto.Post> scheduleLists = requestBody.getScheduleList();
        long memberId = memberService.getMemberIdFromToken(token);
        Schedule schedule = mapper.schedulePostDtoToSchedule(requestBody);
        ScheduleDto.Response response = scheduleService.updateSchedule(memberId, scheduleId, schedule, scheduleLists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "일정 삭제")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity deleteSchedule(@PathVariable("scheduleId") int scheduleId,
                               @RequestHeader(value = "Authorization") String token){
        long memberId = memberService.getMemberIdFromToken(token);
        scheduleService.deleteSchedule(memberId, scheduleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "내가 작성한 일정 목록 조회")
    @GetMapping("/my")
    public ResponseEntity getMySchedules(/*final Pageable pageablePageSize,*/ long cursor,
                                               @RequestHeader(value = "Authorization") String token){
        Member member = memberService.findMemberByToken(token);
        List<ScheduleDto.ListResponse> mySchedules = scheduleService.findMyScheduleByPage(cursor, member, PageRequest.of(0, PAGE_DEFAULT_SIZE));
        return new ResponseEntity<>(mySchedules, HttpStatus.OK);
    }

    @ApiOperation(value = "모든 공유 일정 목록 조회")
    @GetMapping
    public ResponseEntity getAllSchedules(long cursor,
                                          @RequestHeader(value = "Authorization", required = false) String token){
        long memberId = 0;
        if(token != null){
            memberId = memberService.getMemberIdFromToken(token);
        }
        List<ScheduleDto.ListResponse> allSchedules = scheduleService.findSharedSchedulesByPage(cursor, PageRequest.of(0, PAGE_DEFAULT_SIZE), memberId);
        return new ResponseEntity<>(allSchedules, HttpStatus.OK);
    }

    @ApiOperation(value = "일정 상세 페이지 조회")
    @GetMapping("/{scheduleId}")
    public ResponseEntity getSchedule(@PathVariable("scheduleId") long scheduleId,
                                      @RequestHeader(value = "Authorization", required = false) String token) {
        long memberId = 0;
        if(token != null){
            memberId = memberService.getMemberIdFromToken(token);
        }
        ScheduleDto.Response response = scheduleService.getScheduleDetail(scheduleId, memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
