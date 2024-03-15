package com.ImageTrip.image.controller;

import com.ImageTrip.Schedule.dto.ScheduleDto;
import com.ImageTrip.image.dto.ImageDTO;
import com.ImageTrip.image.entity.Image;
import com.ImageTrip.image.mapper.ImageMapper;
import com.ImageTrip.image.service.ImageService;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/picmap")
@Slf4j
public class ImageController {
    private final ImageService imageService;
    private final ImageMapper imageMapper;
    private final MemberService memberService;

    @Autowired
    public ImageController(ImageService imageService, ImageMapper imageMapper, MemberService memberService) {
        this.imageService = imageService;
        this.imageMapper = imageMapper;
        this.memberService = memberService;
    }

    @GetMapping("/")
    public void main(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 이 예제에서는 Authorization 헤더를 통해 로그인 상태를 간단히 확인합니다.
        // 실제 애플리케이션에서는 토큰을 검증하는 더 복잡한 로직이 필요할 수 있습니다.
        String authToken = request.getHeader("Authorization");

        if (authToken != null && !authToken.isEmpty()) {
            // 사용자가 로그인한 상태라고 가정하고 /mine 으로 리다이렉션합니다.
            response.sendRedirect("/picmap/mine");
        } else {
            // 사용자가 로그인하지 않은 상태라고 가정하고 /shared 로 리다이렉션합니다.
            response.sendRedirect("/picmap/shared");
        }
    }

    @ApiOperation(value = "내 사진 조회")
    @GetMapping("/mine")
    public ResponseEntity getMySchedules(@RequestParam(name = "searchTerm", required = false) String searchTerm, @RequestHeader(value = "Authorization") String token){
        long memberId = memberService.getMemberIdFromToken(token);
        List<Image> images = imageService.findAllBySearchAndMember(searchTerm, memberId);

        return ResponseEntity.ok(images);
    }

    @ApiOperation(value = "공유된 사진 조회")
    @GetMapping("/shared")
    public ResponseEntity<ImageDTO.SharedImagesResponse> sharedImages(@RequestParam(name = "searchTerm", required = false) String searchTerm, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Long memberId = (token == null) ? null : memberService.getMemberIdFromToken(token);

        List<Image> images = imageService.findAllBySearchAndShared(searchTerm, true);
        ImageDTO.SharedImagesResponse  sharedImagesResponse = new ImageDTO.SharedImagesResponse(images, memberId);

        return ResponseEntity.ok(sharedImagesResponse);
    }


    @ApiOperation(value = "사진 업로드")
    @PostMapping("/uploadModal")
    public ResponseEntity<?> uploadImages(@RequestPart(value = "files") MultipartFile file,
//                                          @RequestPart(value = "dto") ImageDTO.Upload uploadDto
                                          @RequestParam(value = "tag", required = false) String tag,
                                          @RequestParam(value = "shared", required = false) boolean shared,
                                          @RequestHeader(value = "Authorization") String token){
        // 토큰 검증 및 사용자 인증 (생략)
//        long memberId = memberService.getMemberIdFromToken(token);
        Member member = memberService.findMemberByToken(token);

        try {

            System.out.println("1");
//            System.out.println(files);
//            System.out.println(tag);
//            System.out.println(shared);
            ImageDTO.Upload uploadDto = new ImageDTO.Upload();
//            uploadDto.setFile(file);
            uploadDto.setTag(tag);
            uploadDto.setShared(shared? shared : false);

//            List<String> failedUploads = imageService.saveImages(uploadDto.getFiles(), uploadDto.getTag(), uploadDto.isShared(), memberId);
            String failedUploads = imageService.saveImages(file, uploadDto.getTag(), uploadDto.getShared(), member);
            System.out.println(failedUploads);
            if (failedUploads == null || failedUploads.isEmpty()) {
                return ResponseEntity.ok("All images uploaded successfully.");
            } else {
                return ResponseEntity.badRequest().body("Failed to upload images: " + failedUploads);
            }
        } catch (Exception e) {
            System.out.println("에러발생");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @ApiOperation(value = "사진 수정")
    @PatchMapping("/updateModal/{imageId}")
    public ResponseEntity update(@PathVariable long imageId,
                                 @RequestPart(value = "file", required = false) MultipartFile file,
                                 @RequestParam(required = false) String tag,
                                 @RequestParam(required = false) Boolean shared
                                    , @RequestHeader(value = "Authorization") String token){
        // 토큰 검증 및 사용자 인증 (생략)
        Member member = memberService.findMemberByToken(token);
        Image image = imageService.findByImageId(imageId);

        String updateTag = tag != null && image.getTag() != tag ? tag : image.getTag();
        boolean updateShared = shared != null && image.isShared() != shared ? shared : image.isShared();

//        ImageDTO.Upload uploadDto = new ImageDTO.Upload();
//        uploadDto.setFile(file);
//        uploadDto.setTag(tag);
//        uploadDto.setShared(shared);

//        Image image = imageMapper.imageUpdateDtoToImage(requestBody);
        if(file != null && !file.isEmpty()){
            imageService.deleteByImageId(imageId);
            imageService.saveImages(file, updateTag, updateShared, member);
        } else {
            imageService.updateImage(imageId, updateTag, updateShared);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "사진 삭제")
    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity deleteSchedule(@PathVariable("imageId") int imageId){
        imageService.deleteByImageId(imageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}


