package com.ImageTrip.member.controller;

import com.ImageTrip.member.dto.MemberDto;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.mapper.MemberMapper;
import com.ImageTrip.member.mapper.MemberMapperImpl;
import com.ImageTrip.member.repository.MemberRepository;
import com.ImageTrip.member.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    private MemberMapper mockMapper = Mockito.mock(MemberMapperImpl.class);
    private MemberService mockMemberService = Mockito.mock(MemberService.class);

    @Autowired
    private MemberService memberService1;
    @BeforeAll
    public static void createMemberForTest(){
        ApplicationContext context = new AnnotationConfigApplicationContext(); // AppConfig는 스프링 설정 클래스
        MemberService memberService = context.getBean(MemberService.class);
        Member member = new Member("savedMember@email.com", "savedMemberName", "savedMemberPW");
        memberService.createMember(member);
    }
    @Test
    public void checkUsableEmailResponseTest() throws Exception {
        //given
        MemberDto.CheckEmailDto checkEmailDto = new MemberDto.CheckEmailDto("test@email.com");
        String content = gson.toJson(checkEmailDto);

        //doNothing().when(mockMemberService).verifyExistsEmail(any(String.class));

        //when
        ResultActions actions =
                mockMvc.perform(
                        post("/user/checkUsableEmail")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                        .andExpect(status().isOk());
    }

    @Test
    public void checkUsableNameResponseTest() throws Exception {
        //given
        MemberDto.CheckNameDto checkNameDto = new MemberDto.CheckNameDto("testName");
        String content = gson.toJson(checkNameDto);

        doNothing().when(mockMemberService).verifyExistsName(any(String.class));

        //when
        ResultActions actions =
                mockMvc.perform(
                                post("/user/checkUsableName")
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content)
                        )
                        .andExpect(status().isOk());
    }


    @Test
    public void joionInResponseTest() throws Exception {
        //given
        MemberDto.CreateMemberDto createMemberDto = new MemberDto.CreateMemberDto("test@email.com","testName","testPW1!");
        String content = gson.toJson(createMemberDto);

        Member testMember = new Member("test@mail.com","testName","testPW1!");
        given(mockMapper.createMemberDtoToMember(any(MemberDto.CreateMemberDto.class))).willReturn(testMember);
        doNothing().when(mockMemberService).createMember(any(Member.class));


        //when,then
        ResultActions actions =
                mockMvc.perform(
                        post("/user/joinIn")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andExpect(status().isCreated());
    }

//    @Test
//    public void checkPassword() throws Exception {
//        //given
//        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n";
//        MemberDto.CheckPasswordDto checkPasswordDto = new MemberDto.CheckPasswordDto("testPassword1!");
//        String content = gson.toJson(checkPasswordDto);
//
//        doNothing().when(mockMemberService).checkUserPassword(any(String.class), any(String.class));
//
//        ResultActions actions =
//                mockMvc.perform(
//                        post("/user/checkPassword")
//                                .accept(MediaType.APPLICATION_JSON)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + token)
//                                .content(content)
//                )
//                        .andExpect(status().isOk());
//
//    }




}
