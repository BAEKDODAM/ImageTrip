package com.ImageTrip.imagePost.mapper;

import com.ImageTrip.image.dto.ImageDTO;
import com.ImageTrip.image.entity.Image;
import com.ImageTrip.imagePost.dto.ImagePostDTO;
import com.ImageTrip.imagePost.entity.ImagePost;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImagePostMapper {

    ImagePost imagePostUploadDTOToImagePost(ImagePostDTO.Upload imageUploadDto);
    ImagePost imagePostUpdateDTOToImagePost(ImagePostDTO.Update imageUpdateDTO);

    // 필요한 경우, List 변환 메소드도 추가
//    List<ImageDTO> entitiesToDtos(List<Image> images);
//
//    List<Image> dtosToEntities(List<ImageDTO> dtos);
}

