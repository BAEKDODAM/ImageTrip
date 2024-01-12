package com.ImageTrip.ScheduleList.service;

import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.ScheduleList.dto.ScheduleListDto;
import com.ImageTrip.ScheduleList.entity.ScheduleList;
import com.ImageTrip.ScheduleList.repository.ScheduleListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleListService {
    private final ScheduleListRepository repository;

    public ScheduleListService(ScheduleListRepository repository) {
        this.repository = repository;
    }

    public List<ScheduleList> saveScheduleLists(List<ScheduleListDto.Post> schedules, Schedule schedule) {
        return (List<ScheduleList>) schedules.stream().map(scheduleList -> {
            ScheduleList scheduleList1 = new ScheduleList();
            scheduleList1.setContent(scheduleList.getContent());
            scheduleList1.setLat(scheduleList.getLat());
            scheduleList1.setLon(scheduleList.getLon());
            int priority = schedules.indexOf(scheduleList);
            scheduleList1.setPriority(priority);
            scheduleList1.setSchedule(schedule);
            return repository.save(scheduleList1);
        });
    }

    public List<ScheduleList> changeScheduleLists(long scheduleId, List<ScheduleListDto.Post> schedules, Schedule schedule){
        deleteScheduleListsByScheduleId(scheduleId);
        return saveScheduleLists(schedules, schedule);
    }
    public void deleteScheduleListsByScheduleId(long scheduleId){
        repository.deleteAllByScheduleScheduleId(scheduleId);
        List<ScheduleList> findScheduleLists = repository.findAllByScheduleScheduleId(scheduleId);
        findScheduleLists.stream()
                .forEach(scheduleList -> repository.delete(scheduleList));
    }

}
