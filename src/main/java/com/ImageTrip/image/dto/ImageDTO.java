package com.ImageTrip.image.dto;

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

    @Getter
    public static class Upload {
        private List<MultipartFile> files;
        private boolean isShared;
        private String tag;
    }



//    @Getter
//    public static class Update{
//        private List<MultipartFile> files;
//        private float lat;
//        private float lon;
//    }
}
