package com.ImageTrip.ScheduleLike.controller;

import com.ImageTrip.Response.MultiResponseDto;
import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/like")
public class LikeController {
    @ApiOperation(value = "좋아요")
    @PostMapping("/{scheduleId}")
    public ResponseEntity postLike(@PathVariable("scheduleId") int scheduleId,
                                   @RequestHeader(value = "Authorization", required = false) String token) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "좋아요 취소")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity deleteLike(@PathVariable("scheduleId") int scheduleId,
                                     @RequestHeader(value = "Authorization", required = false) String token) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "좋아요 한 일정 조회")
    @GetMapping()
    public ResponseEntity getLikeSchedules(final Pageable pageablePageSize,
                                           @RequestHeader(value = "Authorization") String token) {
        Sort sort = Sort.by(Sort.Direction.DESC, "startDate"); // challengeId를 내림차순으로 정렬하는 Sort 객체 생성
        Pageable pageable = PageRequest.of(pageablePageSize.getPageNumber(), pageablePageSize.getPageSize(), sort);
        Page<Schedule> schedulePage = null;
        List<ScheduleDto.ListResponse> response = null;

        return new ResponseEntity<>(new MultiResponseDto<>(response, schedulePage), HttpStatus.OK);

    }
}