package com.ImageTrip.schedule.service;

import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.Schedule.repository.ScheduleRepository;
import com.ImageTrip.Schedule.service.ScheduleService;
import com.ImageTrip.ScheduleLike.repository.LikeRepository;
import com.ImageTrip.ScheduleLike.service.LikeService;
import com.ImageTrip.ScheduleList.dto.ScheduleListDto;
import com.ImageTrip.ScheduleList.entity.ScheduleList;
import com.ImageTrip.ScheduleList.service.ScheduleListService;
import com.ImageTrip.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleServiceTest {
    @Mock
    ScheduleRepository scheduleRepository;
    @Mock
    LikeService likeService;
    @Mock
    ScheduleListService scheduleListService;
    @InjectMocks
    ScheduleService scheduleService;

    @Test
    public void createServiceTest(){
        Member member = new Member("email@email", "name", "password");
        Schedule postSchedule = new Schedule("title",true, LocalDate.of(2024,01,01), LocalDate.of(2024,01,01), member);
        List<ScheduleListDto.Post> scheduleLists = new ArrayList<>();
        ScheduleListDto.Post scheduleListPost = new ScheduleListDto.Post("content",1,1);
        scheduleLists.add(scheduleListPost);

        Schedule savedSchedule = new Schedule(25L, "title",true, LocalDateTime.now(), LocalDateTime.now(), LocalDate.of(2024,1,1), LocalDate.of(2024,1,2),new ArrayList<>(), new ArrayList<>(), member);

        List<ScheduleList> savedScheduleLists = new ArrayList<>();
        ScheduleList scheduleList = new ScheduleList("content",1,1,1,savedSchedule);
        scheduleList.setScheduleListId(1L);
        savedScheduleLists.add(scheduleList);

        given(scheduleRepository.save(eq(postSchedule))).willReturn(savedSchedule);
        given(scheduleListService.saveScheduleLists(Mockito.anyList(), Mockito.any(Schedule.class))).willReturn(savedScheduleLists);

        ScheduleDto.Response response = scheduleService.createSchedule(postSchedule, scheduleLists, member);

        assertNotNull(response);
        assertEquals(savedScheduleLists, response.getScheduleLists());
    }
    @Test
    public void updateScheduleTest(){
        long memberId = 1L;
        long scheduleId = 1L;
        Schedule postSchedule = new Schedule();
        postSchedule.setTitle("new title");
        List<ScheduleListDto.Post> scheduleLists = new ArrayList<>();

        Member member = new Member("email","name","password");
        member.setMemberId(memberId);
        Schedule findSchedule = new Schedule(25L, "title",true, LocalDateTime.now(), LocalDateTime.now(), LocalDate.of(2024,1,1), LocalDate.of(2024,1,2),new ArrayList<>(), new ArrayList<>(), member);

        given(scheduleRepository.findByScheduleId(eq(scheduleId))).willReturn(Optional.of(findSchedule));
        given(scheduleRepository.save(Mockito.any(Schedule.class))).willReturn(findSchedule);
        given(scheduleListService.changeScheduleLists(eq(scheduleId),Mockito.anyList(), Mockito.any(Schedule.class))).willReturn(new ArrayList<>());
        given(likeService.scheduleLikeCnt(eq(scheduleId))).willReturn(5);
        given(likeService.hasLiked(eq(scheduleId), eq(memberId))).willReturn(false);

        ScheduleDto.Response response = scheduleService.updateSchedule(memberId, scheduleId, postSchedule, scheduleLists);

        assertNotNull(response);
        assertEquals(response.getTitle(), postSchedule.getTitle());
    }
    @Test
    public void deleteScheduleTest(){
        long scheduleId = 1L;
        long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);
        Schedule findSchedule = new Schedule();
        findSchedule.setMember(member);
        findSchedule.setScheduleId(scheduleId);

        given(scheduleRepository.findByScheduleId(eq(scheduleId))).willReturn(Optional.of(findSchedule));
        doNothing().when(scheduleListService).deleteScheduleListsByScheduleId(eq(scheduleId));
        doNothing().when(likeService).deleteAllLikeByScheduleId(eq(scheduleId));
        doNothing().when(scheduleRepository).deleteById(eq(scheduleId));

        scheduleService.deleteSchedule(scheduleId, memberId);
    }
    @Test
    public void findMyScheduleByPageTest(){
        Long cursor = 11L;
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule(1L, "title",true,LocalDateTime.now(), LocalDateTime.now(),LocalDate.of(2024,1,1), LocalDate.of(2024,1,3),null,null,new Member("email","name","password"));
        schedules.add(schedule);
        given(scheduleRepository.findByMemberMemberIdOrderByScheduleIdDesc(Mockito.anyLong(), Mockito.any(Pageable.class))).willReturn(schedules);
        given(scheduleRepository.findByMemberMemberIdAndScheduleIdLessThanOrderByScheduleIdDesc(eq(cursor), Mockito.anyLong(), Mockito.any(Pageable.class))).willReturn(schedules);
        given(likeService.scheduleLikeCnt(Mockito.anyLong())).willReturn(1);
        given(likeService.hasLiked(Mockito.anyLong(), Mockito.anyLong())).willReturn(false);

        List<ScheduleDto.ListResponse> response = scheduleService.findMyScheduleByPage(0L,1L, PageRequest.of(0, 10));
        assertNotNull(response);
        assertEquals(response.get(0).getTitle(), schedule.getTitle());
    }
    @Test
    public void getScheduleDetailTest(){
        Schedule findSchedule = new Schedule(1L,"title",true,LocalDateTime.now(),LocalDateTime.now(),LocalDate.of(2024,1,1),LocalDate.of(2024,1,3),null,null,new Member());
        List<ScheduleList> scheduleLists = new ArrayList<>();
        given(scheduleRepository.findByScheduleId(Mockito.anyLong())).willReturn(Optional.of(findSchedule));
        given(likeService.scheduleLikeCnt(Mockito.anyLong())).willReturn(5);
        given(likeService.hasLiked(Mockito.anyLong(),Mockito.anyLong())).willReturn(false);
        given(scheduleListService.findSchduleListByScheduleId(Mockito.anyLong())).willReturn(scheduleLists);

        ScheduleDto.Response response = scheduleService.getScheduleDetail(1L, 1L);

        assertNotNull(response);
        assertEquals(response.getTitle(), findSchedule.getTitle());
    }

}
