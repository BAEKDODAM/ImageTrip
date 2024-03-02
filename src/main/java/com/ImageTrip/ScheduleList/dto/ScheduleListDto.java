package com.ImageTrip.ScheduleList.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
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
        @JsonCreator
        public Post(@JsonProperty("content") String content,
                    @JsonProperty("lat") float lat,
                    @JsonProperty("lon") float lon){
            this.content = content;
            this.lat = lat;
            this.lon = lon;
        }
    }
}
