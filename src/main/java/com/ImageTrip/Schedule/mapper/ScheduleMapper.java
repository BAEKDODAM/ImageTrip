package com.ImageTrip.Schedule.mapper;
import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleMapper {
    ScheduleDto.Response scheduleToScheduleResponseDto(Schedule schedule);
    Schedule schedulePostDtoToSchedule(ScheduleDto.Post schedulePostDto);
    List<ScheduleDto.ListResponse> schedulesToScheduleResponseDtos(List<Schedule> schedules);
}
