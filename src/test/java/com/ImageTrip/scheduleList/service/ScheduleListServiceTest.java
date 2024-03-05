package com.ImageTrip.scheduleList.service;

import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.ScheduleList.dto.ScheduleListDto;
import com.ImageTrip.ScheduleList.entity.ScheduleList;
import com.ImageTrip.ScheduleList.repository.ScheduleListRepository;
import com.ImageTrip.ScheduleList.service.ScheduleListService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleListServiceTest {
    @Mock
    ScheduleListRepository scheduleListRepository;
    @InjectMocks
    ScheduleListService scheduleListService;

    @Test
    public void saveScheduleListsTest(){
        List<ScheduleListDto.Post> postScheduleList = new ArrayList<>();
        ScheduleListDto.Post postList = new ScheduleListDto.Post("content",1,1);
        postScheduleList.add(postList);
        Schedule schedule = new Schedule();

        List<ScheduleList> scheduleLists = new ArrayList<>();
        ScheduleList saveScheduleList = new ScheduleList("content",1,1,0,schedule);
        scheduleLists.add(saveScheduleList);

        given(scheduleListRepository.save(Mockito.any(ScheduleList.class))).willReturn(saveScheduleList);

        List<ScheduleList> response = scheduleListService.saveScheduleLists(postScheduleList, schedule);

        assertNotNull(response);
        assertEquals(response.get(0).getContent(),  postList.getContent());
    }
}
