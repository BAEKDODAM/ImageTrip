package com.ImageTrip.search.contorller;

import com.ImageTrip.member.service.MemberService;
import com.ImageTrip.search.service.SearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @MockBean
    SearchService searchService;

    @Test
    @DisplayName("search schedule test")
    public void searchTest() throws Exception {
        long memberid = 1L;
        String token = "test-token";
        String search = "test-word";
        long cursor = 11;

        given(memberService.getMemberIdFromToken(eq(token))).willReturn(memberid);
        given(searchService.findSearch(eq(search), eq(cursor), eq(PageRequest.of(0, 10)), eq(memberid))).willReturn(new ArrayList<>());

        ResultActions actions =
                mockMvc.perform(
                                post("/search")
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("cursor", String.valueOf(cursor))
                                        .param("search", search)
                                        .header("Authorization", token)
                        )
                        .andExpect(status().isOk());
    }
}
