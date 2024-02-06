package com.ImageTrip.member.controller;

import com.ImageTrip.member.dto.MemberDto;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.mapper.MemberMapper;
import com.ImageTrip.member.mapper.MemberMapperImpl;
import com.ImageTrip.member.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    private MemberMapper mockMapper = Mockito.mock(MemberMapperImpl.class);
    private MemberService mockMemberService = Mockito.mock(MemberService.class);

//    @Test
//    public void checkUseableEmailTest() throws Exception {
//        //given
//        MemberDto.CheckEmailDto checkEmailDto = new MemberDto.CheckEmailDto("test@email.com");
//        String content = gson.toJson(checkEmailDto);
//
//        doNothing().when(mockMemberService).verifyExistsEmail(any(String.class));
//
//        //when
//        ResultActions actions =
//                mockMvc.perform(
//                        post("/user/checkUseableEmail")
//                                .accept(MediaType.APPLICATION_JSON)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(content)
//                )
//                        .andExpect(status().isOk());
//    }


    @Test
    public void joionInTest() throws Exception {
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




}
