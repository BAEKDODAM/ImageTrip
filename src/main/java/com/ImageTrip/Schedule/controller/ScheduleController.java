package com.ImageTrip.Schedule.controller;

import com.ImageTrip.Response.MultiResponseDto;
import com.ImageTrip.Response.PageInfo;
import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.Schedule.mapper.ScheduleMapper;
import com.ImageTrip.Schedule.service.ScheduleService;
import com.ImageTrip.ScheduleList.dto.ScheduleListDto;
import com.ImageTrip.ScheduleList.entity.ScheduleList;
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
    private static final int PAGE_DEFAULT_SIZE = 10;

    public ScheduleController(ScheduleService scheduleService, ScheduleMapper mapper) {
        this.scheduleService = scheduleService;
        this.mapper = mapper;
    }

    @PostMapping
    @ApiOperation(value = "일정 생성")
    public ResponseEntity postSchedule(@Valid @RequestBody ScheduleDto.Post requestBody,
                                       @RequestHeader(value = "Authorization") String token) throws Exception, IOException {
        requestBody.setScheduleList(null);
        Schedule postSchedule = mapper.schedulePostDtoToSchedule(requestBody);
        long memberId = 1L;//jwtTokenizer.getUserId(token);
        List<ScheduleListDto.Post> scheduleList = requestBody.getScheduleList();
        ScheduleDto.Response response = scheduleService.createSchedule(1L, postSchedule, scheduleList);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "일정 수정")
    @PatchMapping("/{scheduleId}")
    public ResponseEntity patchSchedule(@PathVariable("scheduleId") int scheduleId,
                                        @RequestBody ScheduleDto.Post requestBody,
                                        @RequestHeader(value = "Authorization") String token) {
        requestBody.setScheduleList(null);
        long memberId = 1L;//jwtTokenizer.getUserId(token);
        Schedule schedule = mapper.schedulePostDtoToSchedule(requestBody);
        List<ScheduleListDto.Post> scheduleLists = requestBody.getScheduleList();
        ScheduleDto.Response response = scheduleService.updateSchedule(memberId, scheduleId, schedule, scheduleLists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "일정 삭제")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity deleteSchedule(@PathVariable("scheduleId") int scheduleId,
                               @RequestHeader(value = "Authorization") String token){
        long memberId = 1L;//jwtTokenizer.getUserId(token);
        scheduleService.deleteSchedule(memberId, scheduleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "내가 작성한 일정 목록 조회")
    @GetMapping("/my")
    public ResponseEntity getMySchedules(/*final Pageable pageablePageSize,*/ long cursor,
                                               @RequestHeader(value = "Authorization") String token){
        long memberId = 1L;//jwtTokenizer.getUserId(token);
        List<ScheduleDto.ListResponse> mySchedules = scheduleService.findMyScheduleByPage(cursor, memberId, PageRequest.of(0, PAGE_DEFAULT_SIZE));
        return new ResponseEntity<>(mySchedules, HttpStatus.OK);
    }

    @ApiOperation(value = "모든 공유 일정 목록 조회")
    @GetMapping
    public ResponseEntity getAllSchedules(long cursor){
        List<ScheduleDto.ListResponse> allSchedules = scheduleService.findSharedSchedulesByPage(cursor, PageRequest.of(0, PAGE_DEFAULT_SIZE));
        return new ResponseEntity<>(allSchedules, HttpStatus.OK);
    }

    @ApiOperation(value = "일정 상세 페이지 조회")
    @GetMapping("/{scheduleId}")
    public ResponseEntity getSchedule(@PathVariable("scheduleId") long scheduleId) {
        ScheduleDto.Response response = scheduleService.getScheduleDetail(scheduleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
