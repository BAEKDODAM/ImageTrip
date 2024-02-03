package com.ImageTrip.image.mapper;

import com.ImageTrip.image.dto.ImageDTO;
import com.ImageTrip.image.entity.Image;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDTO.Upload entityToUploadDto(Image image);

    Image imageUploadDtoToImage(ImageDTO.Upload imageUploadDto);
    Image imageUpdateDtoToImage(ImageDTO.Update imageUpdateDto);

    // 필요한 경우, List 변환 메소드도 추가
//    List<ImageDTO> entitiesToDtos(List<Image> images);
//
//    List<Image> dtosToEntities(List<ImageDTO> dtos);
}
