package com.ImageTrip.search.service;

import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.service.ScheduleService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    private final ScheduleService scheduleService;

    public SearchService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    public List<ScheduleDto.ListResponse> findSearch(String search, long cursor, Pageable pageable, long memberId) {
        return scheduleService.findSearchSchedule(cursor, pageable, search, memberId);
    }
}
