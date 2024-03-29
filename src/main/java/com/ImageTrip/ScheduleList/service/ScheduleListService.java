package com.ImageTrip.ScheduleList.service;

import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.ScheduleList.dto.ScheduleListDto;
import com.ImageTrip.ScheduleList.entity.ScheduleList;
import com.ImageTrip.ScheduleList.repository.ScheduleListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@Service
public class ScheduleListService {
    private final ScheduleListRepository repository;

    public ScheduleListService(ScheduleListRepository repository) {
        this.repository = repository;
    }

    public List<ScheduleList> saveScheduleLists(List<ScheduleListDto.Post> schedules, Schedule schedule) {
        return schedules.stream().map(scheduleList -> {
            ScheduleList scheduleList1 = new ScheduleList(scheduleList.getContent(), scheduleList.getLat(), scheduleList.getLon(),schedules.indexOf(scheduleList), schedule);
            return repository.save(scheduleList1);
        }).collect(Collectors.toList());
    }

    public List<ScheduleList> changeScheduleLists(long scheduleId, List<ScheduleListDto.Post> schedules, Schedule schedule){
        deleteScheduleListsByScheduleId(scheduleId);
        return saveScheduleLists(schedules, schedule);
    }
    public void deleteScheduleListsByScheduleId(long scheduleId){
        repository.deleteAllByScheduleScheduleId(scheduleId);
        /*List<ScheduleList> findScheduleLists = repository.findAllByScheduleScheduleId(scheduleId);
        findScheduleLists.stream()
                .forEach(scheduleList -> repository.delete(scheduleList));*/
    }
    public List<ScheduleList> findSchduleListByScheduleId(long scheduleId){
        return repository.findAllByScheduleScheduleId(scheduleId);
    }
}
