package com.ImageTrip.like.service;

import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.Schedule.repository.ScheduleRepository;
import com.ImageTrip.ScheduleLike.entity.ScheduleLike;
import com.ImageTrip.ScheduleLike.repository.LikeRepository;
import com.ImageTrip.ScheduleLike.service.LikeService;
import com.ImageTrip.exception.BusinessLogicException;
import com.ImageTrip.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

@SpringBootTest
public class LikeServiceTest {
    @InjectMocks
    LikeService likeService;
    @Mock
    ScheduleRepository scheduleRepository;
    @Mock
    LikeRepository likeRepository;

    @Test
    public void createLikeTest(){
        given(scheduleRepository.findByScheduleId(Mockito.anyLong())).willReturn(Optional.of(new Schedule()));
        given(likeRepository.findByScheduleScheduleIdAndMemberMemberId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.empty());
        given(likeRepository.save(Mockito.any(ScheduleLike.class))).willReturn(new ScheduleLike());

        likeService.createLike(1L, new Member());
    }
    @Test void throwExceptionAlreadyLikedTest(){
        Schedule findSchedule = new Schedule();
        Member member = new Member();
        ScheduleLike like = new ScheduleLike(1L, findSchedule, member);
        given(scheduleRepository.findByScheduleId(Mockito.anyLong())).willReturn(Optional.of(findSchedule));
        given(likeRepository.findByScheduleScheduleIdAndMemberMemberId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(like));
        given(likeRepository.save(Mockito.any(ScheduleLike.class))).willReturn(new ScheduleLike());

        assertThrows(BusinessLogicException.class, ()->likeService.createLike(1L,member));

    }
    @Test
    public void findLikeScheduleTest(){
        Member member1 = new Member();
        Member member2 = new Member();
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule(1L,"title",true, LocalDateTime.now(),LocalDateTime.now(), LocalDate.of(2024,1,1),LocalDate.of(2024,1,3),null,null, member1);
        schedules.add(schedule);

        // 현재 게시물 좋아요 한 사람
        List<ScheduleLike> likeList = new ArrayList<>();
        ScheduleLike like = new ScheduleLike(1L, schedule, member2);
        likeList.add(like);

        // member 좋아요 리스트
        List<ScheduleLike> memberLikeList = new ArrayList<>();
        ScheduleLike like1 = new ScheduleLike(2L, schedule, member1);
        memberLikeList.add(like1);
        schedules.get(0).setScheduleLikes(memberLikeList);

        given(likeRepository.findByMemberMemberIdOrderByScheduleLikeIdDesc(Mockito.anyLong(), Mockito.any(Pageable.class))).willReturn(memberLikeList);
        given(likeRepository.findByMemberMemberIdAndScheduleLikeIdLessThanOrderByScheduleLikeIdDesc(Mockito.anyLong(), eq(11),  Mockito.any(Pageable.class))).willReturn(memberLikeList);
        given(likeRepository.findAllByScheduleScheduleId(Mockito.anyLong())).willReturn(likeList);
        given(likeRepository.findByScheduleScheduleIdAndMemberMemberId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.empty());

        List<ScheduleDto.ListResponse> response = likeService.findLikeScheduleByPage(0L, PageRequest.of(0, 10),1L);
        assertNotNull(response);
        assertEquals(response.get(0).getLikeCnt(), 1);
    }
}
