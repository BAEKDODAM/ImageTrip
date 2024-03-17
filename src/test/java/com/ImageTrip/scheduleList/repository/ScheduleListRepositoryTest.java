package com.ImageTrip.scheduleList.repository;

import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.Schedule.repository.ScheduleRepository;
import com.ImageTrip.ScheduleList.entity.ScheduleList;
import com.ImageTrip.ScheduleList.repository.ScheduleListRepository;
import com.ImageTrip.auth.jwt.JwtTokenizer;
import com.ImageTrip.auth.utils.CustomAuthorityUtils;
import com.ImageTrip.config.SecurityConfiguration;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Transactional
public class ScheduleListRepositoryTest {
    @Autowired
    ScheduleListRepository scheduleListRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void findAllByScheduleScheduleIdTest(){
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

        ScheduleList scheduleList = new ScheduleList("content",1,1,1,schedule);
        scheduleListRepository.save(scheduleList);

        long scheduleId = schedule.getScheduleId();
        List<ScheduleList> scheduleLists = scheduleListRepository.findAllByScheduleScheduleId(scheduleId);
        assertEquals(scheduleLists.get(0).getContent(), scheduleList.getContent());
    }

    @Test
    public void deleteAllByScheduleScheduleIdTest(){
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
        Schedule saveSchedule = scheduleRepository.save(schedule);

        ScheduleList scheduleList = new ScheduleList("content",1,1,1,schedule);
        scheduleListRepository.save(scheduleList);

        long scheduleId = saveSchedule.getScheduleId();
        scheduleListRepository.deleteAllByScheduleScheduleId(scheduleId);

        assertEquals(scheduleListRepository.findAll(),new ArrayList<>());
    }
}
