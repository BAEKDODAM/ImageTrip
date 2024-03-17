package com.ImageTrip.like.controller;

import com.ImageTrip.Schedule.mapper.ScheduleMapper;
import com.ImageTrip.ScheduleLike.service.LikeService;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LikeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;
    @MockBean
    MemberService memberService;

    @MockBean
    LikeService likeService;
    @MockBean
    ScheduleMapper scheduleMapper;

    @Test
    @DisplayName("make like test")
    public void makeLikeTest() throws Exception {
        String token = "test-token";
        long scheduleId = 1L;

        given(memberService.findMemberByToken(eq(token))).willReturn(new Member());
        doNothing().when(likeService).createLike(eq(scheduleId), Mockito.any(Member.class));

        URI postUri = UriComponentsBuilder.newInstance().path("/like/{scheduleId}").buildAndExpand(scheduleId).toUri();

        ResultActions actions =
                mockMvc.perform(
                                post(postUri)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .header("Authorization", token)

                        )
                        .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("delete like test")
    public void deleteLikeTest() throws Exception {
        String token = "test-token";
        long scheduleId = 1L;
        long memberId = 1L;

        given(memberService.getMemberIdFromToken(eq(token))).willReturn(memberId);
        doNothing().when(likeService).deleteLike(eq(scheduleId), eq(memberId));

        URI postUri = UriComponentsBuilder.newInstance().path("/like/{scheduleId}").buildAndExpand(scheduleId).toUri();

        ResultActions actions =
                mockMvc.perform(
                                delete(postUri)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .header("Authorization", token)

                        )
                        .andExpect(status().isOk());

    }

    @Test
    @DisplayName("get like Schedule test")
    public void getLikeScheduleTest() throws Exception {
        long cursor = 1L;
        long memberId = 1L;
        String token = "test-token";

        given(memberService.getMemberIdFromToken(eq(token))).willReturn(memberId);
        given(likeService.findLikeScheduleByPage(eq(cursor), eq(PageRequest.of(0, 11)), eq(memberId))).willReturn(new ArrayList<>());

        ResultActions actions =
                mockMvc.perform(
                                get("/like")
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("cursor", String.valueOf(cursor))
                                        .header("Authorization", token)
                        )
                        .andExpect(status().isOk());

    }
}
