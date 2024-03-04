package com.ImageTrip.ScheduleList.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ScheduleListDto {
    @Getter
    @Setter
    public static class Post {
        private String content;
        private float lat;
        private float lon;
    }
}
