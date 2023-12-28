package com.ImageTrip.Schedule.service;

import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    public ScheduleDto.Response createSchedule(long l, Schedule postSchedule) {
        return new ScheduleDto.Response();
    }
}
