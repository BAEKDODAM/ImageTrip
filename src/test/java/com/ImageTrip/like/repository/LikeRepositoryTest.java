package com.ImageTrip.like.repository;

import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.Schedule.repository.ScheduleRepository;
import com.ImageTrip.ScheduleLike.entity.ScheduleLike;
import com.ImageTrip.ScheduleLike.repository.LikeRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class LikeRepositoryTest {
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ScheduleRepository scheduleRepository;

    @BeforeEach
    void before(){
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

        // save like
        ScheduleLike scheduleLike = new ScheduleLike(schedule, member);
        likeRepository.save(scheduleLike);
    }

    @Test
    public void findAllByScheduleScheduleIdTest(){
        long scheduleId = scheduleRepository.findAll().get(0).getScheduleId();

        List<ScheduleLike> response = likeRepository.findAllByScheduleScheduleId(scheduleId);
        assertNotNull(response);
    }

    @Test
    public void findByScheduleScheduleIdAndMemberMemberIdTest(){
        long memberId = memberRepository.findAll().get(0).getMemberId();
        long scheduleId = scheduleRepository.findAll().get(0).getScheduleId();
        long likeId = likeRepository.findAll().get(0).getScheduleLikeId();

        Optional<ScheduleLike> response = likeRepository.findByScheduleScheduleIdAndMemberMemberId(scheduleId,memberId);
        assertNotNull(response);
        assertEquals(response.get().getScheduleLikeId(), likeId);
    }

    @Test
    @Transactional
    public void findByMemberMemberIdOrderByScheduleLikeIdDescTest(){
        Member member = memberRepository.findAll().get(0);
        Schedule schedule = scheduleRepository.findAll().get(0);

        for(int i=0; i<11; i++){
            ScheduleLike scheduleLike = new ScheduleLike(schedule, member);
            likeRepository.save(scheduleLike);
        }

        List<ScheduleLike> likeList = likeRepository.findByMemberMemberIdOrderByScheduleLikeIdDesc(member.getMemberId(), PageRequest.of(0,10));

        assertEquals(likeList.size(), 10);
    }
    @Test
    public void findByMemberMemberIdAndScheduleLikeIdLessThanOrderByScheduleLikeIdDescTest(){
        Member member= memberRepository.findAll().get(0);
        Schedule schedule = scheduleRepository.findAll().get(0);

        for(int i=0; i<11; i++){
            ScheduleLike scheduleLike = new ScheduleLike(schedule, member);
            likeRepository.save(scheduleLike);
        }
        long cursor = 15L;
        List<ScheduleLike> likeList = likeRepository.findByMemberMemberIdAndScheduleLikeIdLessThanOrderByScheduleLikeIdDesc(member.getMemberId(), cursor,PageRequest.of(0,10));

        long length = likeList.get(0).getScheduleLikeId();
        for (int i=0; i<10; i++){
            assertEquals(likeList.get(i).getScheduleLikeId(), length-i);
        }
    }
    @Test
    @Transactional
    public void deleteAllByScheduleScheduleIdTest(){
        likeRepository.deleteAllByScheduleScheduleId(1L);
        List<ScheduleLike> find = likeRepository.findAllByScheduleScheduleId(1L);
        assertEquals(find, new ArrayList<>());
    }

    @Test
    @Transactional
    public void deleteByScheduleScheduleIdAndMemberMemberId(){
        long memberId = memberRepository.findAll().get(0).getMemberId();
        long scheduleId = scheduleRepository.findAll().get(0).getScheduleId();

        likeRepository.deleteByScheduleScheduleIdAndMemberMemberId(scheduleId, memberId);
        boolean isPresent = likeRepository.findByScheduleScheduleIdAndMemberMemberId(scheduleId, memberId).isPresent();
        assertEquals(isPresent, false);
    }

}
