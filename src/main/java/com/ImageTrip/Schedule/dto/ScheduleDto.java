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

@NoArgsConstructor
public class ScheduleDto {
    @Getter
    @Setter
    public static class Post {
        private String title;

        @Setter
        @Getter
        private List<ScheduleListDto.Post> scheduleList;

        private LocalDate startDate;

        private LocalDate endDate;

        private Boolean share;
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private long scheduleId;
        @ApiModelProperty(example = "일정 이름")
        private String title;

        private LocalDate startDate;

        private LocalDate endDate;

        private int likeCnt;

        private String name;

        private List<ScheduleList> scheduleLists;
        private boolean share;
        private boolean liked;

        public static Response from(Schedule schedule, int likeCnt, boolean liked){
            return Response.builder()
                    .scheduleId(schedule.getScheduleId())
                    .title(schedule.getTitle())
                    .scheduleLists(schedule.getScheduleLists())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .likeCnt(likeCnt)
                    .liked(liked)
                    .name(schedule.getMember().getName())
                    .share(schedule.getShare())
                    .build();
        }

        public static Response from(Schedule schedule, Member member, List<ScheduleList> scheduleLists, int likeCnt) {
            return Response.builder()
                    .scheduleId(schedule.getScheduleId())
                    .title(schedule.getTitle())
                    .scheduleLists(schedule.getScheduleLists())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .likeCnt(likeCnt)
                    .name(member.getName())
                    .share(schedule.getShare())
                    .build();
        }
    }

    @Builder
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListResponse {
        private long scheduleId;

        private String title;

        private LocalDate startDate;

        private LocalDate endDate;

        private int likeCnt;

        private String name;
        private boolean liked;

        public ListResponse(Schedule schedule) {
        }
        public static ListResponse from (Schedule schedule, int likeCnt, boolean liked){
            return ListResponse.builder()
                    .scheduleId(schedule.getScheduleId())
                    .title(schedule.getTitle())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .likeCnt(likeCnt)
                    .liked(liked)
                    .name(schedule.getMember().getName())
                    .build();
        }

        public static ListResponse from (Schedule schedule, Member member, int likeCnt, boolean liked){
            return ListResponse.builder()
                    .scheduleId(schedule.getScheduleId())
                    .title(schedule.getTitle())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .likeCnt(likeCnt)
                    .liked(liked)
                    .name(member.getName())
                    .build();
        }
    }
}
