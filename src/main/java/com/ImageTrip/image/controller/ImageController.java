package com.ImageTrip.image.controller;

import com.ImageTrip.image.dto.ImageDTO;
import com.ImageTrip.image.entity.Image;
import com.ImageTrip.image.mapper.ImageMapper;
import com.ImageTrip.image.service.ImageService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/picmap")
@Slf4j
public class ImageController {
    private final ImageService imageService;

//    private final ImageMapper imageMapper;

    @Autowired
    public ImageController(ImageService imageService/*, ImageMapper imageMapper*/) {
        this.imageService = imageService;
//        this.imageMapper = imageMapper;
    }

    @GetMapping("/")
    public void main(){
        System.out.println("picmap");
    }


    @PostMapping("/uploadModal")
    public ResponseEntity<?> uploadImages(@Valid @RequestBody ImageDTO.Upload uploadDto/*,
                                          @RequestHeader(value = "Authorization") String token*/){

        // 토큰 검증 및 사용자 인증 (생략)
        long memberId = 1L;//jwtTokenizer.getUserId(token);

        try {

            List<String> failedUploads = imageService.saveImages(uploadDto.getFiles(), uploadDto.getTag(), uploadDto.isShared(), memberId);

            if (failedUploads.isEmpty()) {
                return ResponseEntity.ok("All images uploaded successfully.");
            } else {
                return ResponseEntity.badRequest().body("Failed to upload images: " + failedUploads);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }


//    @PatchMapping("/updateModal/{imageId}")
//    public ResponseEntity update(@PathVariable long imageId, @RequestBody ImageDTO.Update requestBody,
//                                    @RequestHeader(value = "Authorization") String token){
//        ImageDTO.Response response = new ImageDTO.Response();
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }


}


