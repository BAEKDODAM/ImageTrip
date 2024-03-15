package com.ImageTrip.image.dto;

import com.ImageTrip.image.entity.Image;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor
public class ImageDTO {
    @Getter
    public static class Reponse{
        private boolean check;
    }

    @Setter
    @Getter
    public static class Upload {
//        private List<MultipartFile> files;
//        private MultipartFile file;
        private Boolean shared;
        private String tag;
    }



    @Getter
    @ApiModel(description = "이미지 정보 업데이트를 위한 DTO")
    public static class Update{
        private String tag;
        private Boolean shared;
    }

    @Getter
    @NoArgsConstructor
    public static class SharedImagesResponse {
        private List<Image> images;
        private Long memberId;

        public SharedImagesResponse(List<Image> images, Long memberId) {
            this.images = images;
            this.memberId = memberId;
        }
    }
}
