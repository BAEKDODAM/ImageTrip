package com.ImageTrip.schedule.repository;

import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.Schedule.repository.ScheduleRepository;
import com.ImageTrip.auth.jwt.JwtTokenizer;
import com.ImageTrip.auth.utils.CustomAuthorityUtils;
import com.ImageTrip.config.SecurityConfiguration;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class ScheduleRepositoryTest {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void init(){
        // save member
        JwtTokenizer jwtTokenizer = new JwtTokenizer();
        CustomAuthorityUtils authorityUtils = new CustomAuthorityUtils();
        SecurityConfiguration securityConfiguration = new SecurityConfiguration(jwtTokenizer, authorityUtils);
        String password = securityConfiguration.passwordEncoder().encode("tourmate12!");

        Member member = new Member("guest@email.com", "name", password);
        member.setRoles(authorityUtils.createRoles(member.getEmail()));
        Member saveMember = memberRepository.save(member);

        // save schedule
        Schedule schedule = new Schedule("title", true, LocalDate.of(2024,1,1), LocalDate.of(2024,1,3),saveMember);
        scheduleRepository.save(schedule);
    }
    @Test
    public void findByScheduleIdTest(){
        long scheduleId = scheduleRepository.findAll().get(0).getScheduleId();
        Schedule schedule = scheduleRepository.findByScheduleId(scheduleId).orElseThrow();
        assertEquals(schedule.getTitle(),"title");
        assertEquals(schedule.getShare(), true);
    }

    @Test
    public void findByShareTrueOrderByScheduleIdDescTest(){
        List<Schedule> scheduleList = scheduleRepository.findByShareTrueOrderByScheduleIdDesc(PageRequest.of(0,10));
        scheduleList.stream().forEach(schedule -> assertEquals(schedule.getShare(),true));
    }

    @Test
    public void findByShareTrueAndScheduleIdLessThanOrderByScheduleIdDescTest(){
        long cursor = 11L;
        List<Schedule> schedules = scheduleRepository.findByShareTrueAndScheduleIdLessThanOrderByScheduleIdDesc(cursor, PageRequest.of(0,10));
        schedules.stream().forEach(schedule -> assertEquals(schedule.getShare(),true));
    }

    @Test
    public void findByMemberMemberIdOrderByScheduleIdDescTest(){
        long memberId = memberRepository.findAll().get(0).getMemberId();
        List<Schedule> schedules = scheduleRepository.findByMemberMemberIdOrderByScheduleIdDesc(memberId, PageRequest.of(0,10));
        schedules.stream().forEach(schedule -> assertEquals(schedule.getMember().getMemberId(), memberId));
    }
    @Test
    public void findByMemberMemberIdAndScheduleIdLessThanOrderByScheduleIdDescTest(){
        long memberId = memberRepository.findAll().get(0).getMemberId();
        long cursor = 11L;
        List<Schedule> schedules = scheduleRepository.findByMemberMemberIdAndScheduleIdLessThanOrderByScheduleIdDesc(memberId, cursor, PageRequest.of(0,10));
        schedules.stream().forEach(schedule -> assertEquals(schedule.getMember().getMemberId(), memberId));
    }

    @Test
    void findByShareTrueAndTitleContainingOrderByScheduleIdDescTest(){
        String search = "title";
        List<Schedule> scheduleList = scheduleRepository.findByShareTrueAndTitleContainingOrderByScheduleIdDesc(search, PageRequest.of(0,10));
        scheduleList.stream().forEach(schedule -> assertEquals(schedule.getTitle().contains(search),true));
    }
    @Test
    void findByShareTrueAndTitleContainingAndScheduleIdLessThanOrderByScheduleIdDescTest(){
        String search = "title";
        long cursor = 11L;
        List<Schedule> scheduleList = scheduleRepository.findByShareTrueAndTitleContainingAndScheduleIdLessThanOrderByScheduleIdDesc(search, cursor, PageRequest.of(0,10));
        scheduleList.stream().forEach(schedule -> assertEquals(schedule.getTitle().contains(search),true));
    }
}
