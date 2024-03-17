package com.ImageTrip.Schedule.dto;

import com.ImageTrip.Schedule.entity.Schedule;
import com.ImageTrip.ScheduleList.dto.ScheduleListDto;
import com.ImageTrip.ScheduleList.entity.ScheduleList;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
public class ScheduleDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post {
        private String title;

        @Setter
        @Getter
        private List<ScheduleListDto.Post> scheduleList;

        private LocalDate startDate;

        private LocalDate endDate;

        private Boolean share;

        @JsonCreator
        public Post(@JsonProperty("title") String title,
                    @JsonProperty("scheduleList") List<ScheduleListDto.Post> scheduleList,
                    @JsonProperty("startDate") LocalDate startDate,
                    @JsonProperty("endDate") LocalDate endDate,
                    @JsonProperty("share") boolean share){
            this.title = title;
            this.scheduleList = scheduleList;
            this.startDate = startDate;
            this.endDate = endDate;
            this.share = share;
        }
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

        @Builder.Default
        private int likeCnt = 0;

        private String name;

        private List<ScheduleList> scheduleLists;
        private boolean share;
        @Builder.Default
        private boolean liked = false;

        public static Response from(Schedule schedule, List<ScheduleList> scheduleLists, int likeCnt, boolean liked){
            return Response.builder()
                    .scheduleId(schedule.getScheduleId())
                    .title(schedule.getTitle())
                    .scheduleLists(scheduleLists)
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .likeCnt(likeCnt)
                    .liked(liked)
                    .name(schedule.getMember().getName())
                    .share(schedule.getShare())
                    .build();
        }

        public static Response from(Schedule schedule, List<ScheduleList> scheduleLists) {
            return Response.builder()
                    .scheduleId(schedule.getScheduleId())
                    .title(schedule.getTitle())
                    .scheduleLists(scheduleLists)
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .name(schedule.getMember().getName())
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
    }
}
