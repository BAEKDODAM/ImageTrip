package com.ImageTrip.schedule.controller;

import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.Schedule.mapper.ScheduleMapper;
import com.ImageTrip.Schedule.service.ScheduleService;
import com.ImageTrip.ScheduleList.dto.ScheduleListDto;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.service.MemberService;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import com.google.gson.Gson;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc //Controller 테스트를 위한 애플리케이션의 자동 구성 작업
public class ScheduleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;
    @MockBean
    MemberService memberService;

    @MockBean
    ScheduleService scheduleService;
    @MockBean
    ScheduleMapper scheduleMapper;

    @Test
    @DisplayName("create schedule test")
    public void postSchedule() throws Exception {
        /* 중복 */
        // Gson 빌더 생성
        GsonBuilder gsonBuilder = new GsonBuilder();

        // LocalDate 타입에 대한 직렬화 방식 설정
        gsonBuilder.registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                new JsonPrimitive(src.toString())); // LocalDate를 문자열로 직렬화

        // Gson 인스턴스 생성
        Gson gson = gsonBuilder.create();

        String token = "test-token";

        ScheduleListDto.Post listPost = new ScheduleListDto.Post("content",1,1);
        List<ScheduleListDto.Post> list = new ArrayList<>();
        list.add(listPost);

        ScheduleDto.Post post = new ScheduleDto.Post("title", list, LocalDate.of(2024,01,01), LocalDate.of(2024,01,01), true);

        String content = gson.toJson(post);

        ScheduleDto.Response response = new ScheduleDto.Response();
        response.setTitle("title");
        /* 중복 끝 */
        given(memberService.findMemberByToken(eq(token))).willReturn(new Member());
        given(scheduleMapper.schedulePostDtoToSchedule(Mockito.any(ScheduleDto.Post.class))).willReturn(new Schedule());
        given(scheduleService.createSchedule(Mockito.any(Schedule.class), Mockito.any(List.class), Mockito.any(Member.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        post("/schedule")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .header("Authorization", token)
                );
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(post.getTitle()));

        String responseContent = actions.andReturn().getResponse().getContentAsString();
        System.out.println("Response: " + responseContent);
    }

    @Test
    @DisplayName("patch Schedule test")
    public void patchScheduleTest() throws Exception {
        /* 중복 */
        // Gson 빌더 생성
        GsonBuilder gsonBuilder = new GsonBuilder();

        // LocalDate 타입에 대한 직렬화 방식 설정
        gsonBuilder.registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                new JsonPrimitive(src.toString())); // LocalDate를 문자열로 직렬화

        // Gson 인스턴스 생성
        Gson gson = gsonBuilder.create();

        String token = "test-token";

        ScheduleListDto.Post listPost = new ScheduleListDto.Post("content",1,1);
        List<ScheduleListDto.Post> list = new ArrayList<>();
        list.add(listPost);

        ScheduleDto.Post post = new ScheduleDto.Post("title", list, LocalDate.of(2024,01,01), LocalDate.of(2024,01,01), true);

        String content = gson.toJson(post);

        ScheduleDto.Response response = new ScheduleDto.Response();
        response.setTitle("title");
        /* 중복 끝 */
        given(memberService.getMemberIdFromToken(eq(token))).willReturn(1L);
        given(scheduleMapper.schedulePostDtoToSchedule(Mockito.any(ScheduleDto.Post.class))).willReturn(new Schedule());
        given(scheduleService.updateSchedule(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Schedule.class),Mockito.anyList())).willReturn(response);

        URI patchUri = UriComponentsBuilder.newInstance().path("/schedule/{scheduleId}").buildAndExpand(1).toUri();
        ResultActions actions =
                mockMvc.perform(
                        patch(patchUri)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .header("Authorization", token)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(post.getTitle()));
    }

    @Test
    @DisplayName("delete Schedule test")
    public void deleteScheduleTest() throws Exception {
        String token = "test-token";
        long memberId = 1L;

        given(memberService.getMemberIdFromToken(eq(token))).willReturn(memberId);
        doNothing().when(scheduleService).deleteSchedule(eq(memberId), Mockito.anyLong());
        URI deleteUri = UriComponentsBuilder.newInstance().path("/schedule/{scheduleId}").buildAndExpand(1).toUri();

        ResultActions actions =
                mockMvc.perform(
                        delete(deleteUri)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token)

                )
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("get my Shedules test")
    public void getMySchedulesTest() throws Exception {
        long memberId = 1L;
        long cursor = 11;
        String token = "test-token";
        ScheduleDto.ListResponse mockSchedule = new ScheduleDto.ListResponse();
        List<ScheduleDto.ListResponse> mockScheduleList = new ArrayList<>();
        mockScheduleList.add(mockSchedule);

        given(memberService.getMemberIdFromToken(eq(token))).willReturn(memberId);
        given(scheduleService.findMyScheduleByPage(eq(cursor), eq(memberId), eq(PageRequest.of(0,10)))).willReturn(mockScheduleList);

        ResultActions actions =
                mockMvc.perform(
                                get("/schedule/my")
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("cursor", String.valueOf(cursor))
                                        .header("Authorization", token)

                        )
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("get all Schedules test")
    public void getAllSchedulesTest() throws Exception {
        long memberId = 1L;
        long cursor = 11;
        String token = "test-token";

        given(memberService.getMemberIdFromToken(eq(token))).willReturn(memberId);
        given(scheduleService.findSharedSchedulesByPage(anyLong(), eq(PageRequest.of(0,10)), anyLong())).willReturn(new ArrayList<>());

        ResultActions actions =
                mockMvc.perform(
                                get("/schedule")
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("cursor", String.valueOf(cursor))
                        )
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("get Schedule detail page test")
    public void getScheduleDetailTest() throws Exception {
        String token = "test-token";

        given(memberService.getMemberIdFromToken(eq(token))).willReturn(1L);
        given(scheduleService.getScheduleDetail(anyLong(), anyLong())).willReturn(new ScheduleDto.Response());

        URI getUri = UriComponentsBuilder.newInstance().path("/schedule/{scheduleId}").buildAndExpand(1).toUri();

        ResultActions actions =
                mockMvc.perform(
                                get(getUri)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk());

    }
}
