package com.ImageTrip.search.controller;

import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.member.service.MemberService;
import com.ImageTrip.search.service.SearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;
    private final MemberService memberService;
    private static final int PAGE_DEFAULT_SIZE = 10;

    public SearchController(SearchService searchService, MemberService memberService) {
        this.searchService = searchService;
        this.memberService = memberService;
    }

    @PostMapping
    @ApiOperation(value = "일정 검색")
    public ResponseEntity postSearch(@RequestParam("search") String search,
                                     long cursor,
                                     @RequestHeader(value = "Authorization") String token){
        long memberId = 0;
        if(token != null){
            memberId = memberService.getMemberIdFromToken(token);
        }
        List<ScheduleDto.ListResponse> response = searchService.findSearch(search, cursor, PageRequest.of(0, PAGE_DEFAULT_SIZE), memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
