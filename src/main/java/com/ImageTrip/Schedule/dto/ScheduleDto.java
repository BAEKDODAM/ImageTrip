package com.ImageTrip.Schedule.dto;

import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.ScheduleList.dto.ScheduleListDto;
import com.ImageTrip.ScheduleList.entity.ScheduleList;
import com.ImageTrip.member.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleDto {
    @Getter
    public static class Post {
        @NotBlank
        private String title;

        @Setter
        private List<ScheduleListDto.Post> scheduleList;

        private LocalDate startDate;

        private LocalDate endDate;

        private boolean share;
    }

    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @ApiModelProperty(example = "일정 이름")
        private String title;

        private LocalDate startDate;

        private LocalDate endDate;

        private int likeCnt;

        private String name;

        private List<ScheduleList> scheduleLists;
        private boolean share;

        public static Response from(Schedule schedule, int likeCnt){
            return Response.builder()
                    .title(schedule.getTitle())
                    .scheduleLists(schedule.getScheduleLists())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .likeCnt(likeCnt)
                    .name(schedule.getMember().getName())
                    .share(schedule.isShare())
                    .build();
        }

        public static Response from(Schedule schedule, Member member, List<ScheduleList> scheduleLists, int likeCnt) {
            return Response.builder()
                    .title(schedule.getTitle())
                    .scheduleLists(schedule.getScheduleLists())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .likeCnt(likeCnt)
                    .name(member.getName())
                    .share(schedule.isShare())
                    .build();
        }
    }

    @Builder
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListResponse {
        public String title;

        public LocalDate startDate;

        public LocalDate endDate;

        public int likeCnt;

        public String name;

        public ListResponse(Schedule schedule) {
        }
        public static ListResponse from (Schedule schedule, int likeCnt){
            return ListResponse.builder()
                    .title(schedule.getTitle())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .likeCnt(likeCnt)
                    .name(schedule.getMember().getName())
                    .build();
        }

        public static ListResponse from (Schedule schedule, Member member, int likeCnt){
            return ListResponse.builder()
                    .title(schedule.getTitle())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .likeCnt(likeCnt)
                    .name(member.getName())
                    .build();
        }
    }
}
