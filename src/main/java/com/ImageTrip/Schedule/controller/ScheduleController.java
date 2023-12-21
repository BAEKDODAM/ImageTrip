package com.ImageTrip.Schedule.controller;

import com.ImageTrip.Response.MultiResponseDto;
import com.ImageTrip.Response.PageInfo;
import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/*
    여행 일정 생성(상세 내용 전체 받아서), 조회(미리보기-제목, 내용, 작성자), 수정, 삭제
    여행 일정 상세 조회, 수정
 */

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @PostMapping
    @ApiOperation(value = "일정 생성")
    public ResponseEntity postSchedule(@Valid @RequestBody ScheduleDto.Post requestBody,
                                       @RequestHeader(value = "Authorization") String token) throws Exception, IOException {
        ScheduleDto.Response response = new ScheduleDto.Response();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "일정 수정")
    @PatchMapping("/{scheduleId}")
    public ResponseEntity patchSchedule(@PathVariable("scheduleId") int scheduleId,
                                              @RequestBody ScheduleDto.Patch requestBody,
                                @RequestHeader(value = "Authorization") String token){
        ScheduleDto.Response response = new ScheduleDto.Response();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "일정 삭제")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity deleteSchedule(@PathVariable("scheduleId") int scheduleId,
                               @RequestBody ScheduleDto.Patch requestBody,
                               @RequestHeader(value = "Authorization") String token){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "내가 작성한 일정 목록 조회")
    @GetMapping("/my") // 수정필요 무한스크롤
    public ResponseEntity getMySchedules(final Pageable pageablePageSize,
                                               @RequestHeader(value = "Authorization") String token){
        Sort sort = Sort.by(Sort.Direction.DESC, "startDate"); // challengeId를 내림차순으로 정렬하는 Sort 객체 생성
        Pageable pageable = PageRequest.of(pageablePageSize.getPageNumber(), pageablePageSize.getPageSize(), sort);
        Page<Schedule> schedulePage = null; //= ScheduleService.getSchedulesPage(pageable);
        List<ScheduleDto.ListResponse> response = null;// = ScheduleService.getSchedules(pageable);

        return new ResponseEntity<>(new MultiResponseDto<>(response, schedulePage), HttpStatus.OK);
    }

    @ApiOperation(value = "모든 공유 일정 목록 조회")
    @GetMapping
    public ResponseEntity getAllSchedules(final Pageable pageablePageSize){
        Sort sort = Sort.by(Sort.Direction.DESC, "scheduleId"); // challengeId를 내림차순으로 정렬하는 Sort 객체 생성
        Pageable pageable = PageRequest.of(pageablePageSize.getPageNumber(), pageablePageSize.getPageSize(), sort);
        Page<Schedule> schedulePage = null; //= ScheduleService.getSchedulesPage(pageable);
        List<ScheduleDto.ListResponse> response = null;// = ScheduleService.getSchedules(pageable);

        return new ResponseEntity<>(new MultiResponseDto<>(response, schedulePage), HttpStatus.OK);
    }

    @ApiOperation(value = "일정 상세 페이지 조회")
    @GetMapping("/{scheduleId}")
    public ResponseEntity getSchedule(@PathVariable("scheduleId") int scheduleId) {
        ScheduleDto.Response response = new ScheduleDto.Response();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
