package com.ImageTrip.Schedule.dto;

import com.ImageTrip.ScheduleList.entity.ScheduleList;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleDto {
    @Getter
    public static class Post {
        @NotBlank
        public String title;

        @NotBlank
        public String scheduleList;

        public LocalDateTime startDate;

        public LocalDateTime endDate;
    }

    @Setter
    @Getter
    public static class Patch {
        public String title;

        public String scheduleList;

        public LocalDateTime startDate;

        public LocalDateTime endDate;
    }

    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @ApiModelProperty(example = "일정 이름")
        public String title;

        public String scheduleList;

        public LocalDateTime startDate;

        public LocalDateTime endDate;

        public int likeCnt;

        public String nickname;

        public List<ScheduleList> scheduleLists;
    }


    @Setter
    public static class ListResponse {
        public String title;

        public LocalDateTime startDate;

        public LocalDateTime endDate;

        public int likeCnt;

        public String nickname;
    }
}
