package com.ImageTrip.image.service;

import com.ImageTrip.image.dto.ImageDTO;
import com.ImageTrip.image.entity.Image;
import com.ImageTrip.image.repository.ImageRepository;
import com.ImageTrip.member.entity.Member;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.imaging.ImageMetadataReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageService {
    public static final String SEPERATOR  = "_";
    private final ImageRepository imageRepository;
    private final GeoCodingService geoCodingService;
    private final JsonDataService jsonDataService;

    @Autowired
    public ImageService(ImageRepository imageRepository, GeoCodingService geoCodingService, JsonDataService jsonDataService) {
        this.imageRepository = imageRepository;
        this.geoCodingService = geoCodingService; // 추가
        this.jsonDataService = jsonDataService; // 추가
    }

    // 이미지 파일 데이터 베이스에 저장
    // 업로드 실패한 파일 이름 리턴
    public String saveImages(MultipartFile file, String tag, Boolean shared, Member member){

        System.out.println("2");

        Image image = new Image();
        System.out.println(image.getImageId());
        System.out.println(image.getCreatedDate());
        String failedUpload = null;
        UUID uuid = UUID.randomUUID();
//            String type = img.getContentType();   // 굳이 필요하나?

        System.out.println(file);
        String name = uuid.toString() + SEPERATOR + file.getOriginalFilename();
        System.out.println("3");
        System.out.println(name);
        System.out.println(image);
        try (InputStream inputStream = file.getInputStream()) {
            Metadata metadata;

            try{// 이미지 파일의 위치(lat, lon)데이터 가져오기
                metadata = ImageMetadataReader.readMetadata(inputStream);
                GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
                if (gpsDirectory != null) {
                    GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                    if (geoLocation != null) {
                        double latitude = geoLocation.getLatitude();
                        double longitude = geoLocation.getLongitude();
                        System.out.println(latitude);
                        System.out.println(longitude);
//                            String imageSavePath = amazonS3.getUrl(bucketName, name).toString(); //데이터베이스에 저장할 이미지가 저장된 주소

//                            image.setUri(imageSavePath);
                        image.setMember(member);
                        image.setName(name);
                        image.setTag(tag);
                        image.setLat(latitude);
                        image.setLon(longitude);
                        if(shared!=null){image.setShared(shared);}
                        System.out.println(image.getLon());

                        try {
                            // 위치 데이터(위도, 경도)를 api를 이용하여 주소로 변환
                            image.setAddr(geoCodingService.convertAddrKakao(latitude, longitude));
                            System.out.println(image.getAddr());
                        } catch (Exception e) {
                            // 예외 처리 로직
                            // 예를 들어, 로그를 남기거나, 기본 주소를 설정하는 등의 처리를 할 수 있습니다.
                            image.setAddr(null);
                        }
                        System.out.println("4");
                        System.out.println(image.getImageId());
                        imageRepository.save(image);
                    }
                } else{ //위치 데이터가 없을 때
                    failedUpload = file.getOriginalFilename();
                }
            } catch (ImageProcessingException e) {
                // 이미지 처리 중 발생한 예외 처리
                failedUpload = file.getOriginalFilename();
            }

        } catch (IOException e) {
            // 예외 처리
            failedUpload = file.getOriginalFilename();
        }

        return failedUpload;
    }

//    public List<String> saveImages(List<MultipartFile> images, String tag, boolean shared, long memberId){
//        //Member member = memberService.getMemberByMemberId(memberId);
////        Member member = new Member();
//
//        return images.stream().map(img -> {
//            Image image = new Image();
//            String failedUpload = null;
//            UUID uuid = UUID.randomUUID();
////            String type = img.getContentType();   // 굳이 필요하나?
//            String name = uuid.toString() + SEPERATOR + img.getOriginalFilename();
//
//            try (InputStream inputStream = img.getInputStream()) {
//                Metadata metadata;
//                try{// 이미지 파일의 위치(lat, lon)데이터 가져오기
//                    metadata = ImageMetadataReader.readMetadata(inputStream);
//                    GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
//                    if (gpsDirectory != null) {
//                        GeoLocation geoLocation = gpsDirectory.getGeoLocation();
//                        if (geoLocation != null) {
//                            double latitude = geoLocation.getLatitude();
//                            double longitude = geoLocation.getLongitude();
////                            String imageSavePath = amazonS3.getUrl(bucketName, name).toString(); //데이터베이스에 저장할 이미지가 저장된 주소
//
////                            image.setUri(imageSavePath);
//                            image.setMemberId(memberId);
//                            image.setName(name);
//                            image.setTag(tag);
//                            image.setLat(latitude);
//                            image.setLon(longitude);
//                            image.setShared(shared);
//
//                            try {
//                                // 위치 데이터(위도, 경도)를 api를 이용하여 주소로 변환
//                                image.setAddr(geoCodingService.convertAddrKakao(latitude, longitude));
//                            } catch (Exception e) {
//                                // 예외 처리 로직
//                                // 예를 들어, 로그를 남기거나, 기본 주소를 설정하는 등의 처리를 할 수 있습니다.
//                                image.setAddr(null);
//                            }
//
//                            imageRepository.save(image);
//                        }
//                    } else{ //위치 데이터가 없을 때
//                        failedUpload = img.getOriginalFilename();
//                    }
//                } catch (ImageProcessingException e) {
//                    // 이미지 처리 중 발생한 예외 처리
//                    failedUpload = img.getOriginalFilename();
//                }
//
//            } catch (IOException e) {
//                // 예외 처리
//                failedUpload = img.getOriginalFilename();
//            }
//
//            return failedUpload;
//        }).filter(Objects::nonNull)  // null이 아닌 요소만 필터링
//                .collect(Collectors.toList());  // 최종 결과를 리스트로 수집(모든 파일 업로드 성공 시 빈 리스트 반환)
//    }


    @Transactional
    public Image updateImage(long imageId, String tag, boolean shared) {
        // tag와 shared 둘 다 처리하는 로직
        System.out.println("Updating with tag: " + tag + " and shared: " + shared);
        imageRepository.updateUpdatedDate(imageId);
        imageRepository.updateImageTagAndShared(imageId, tag, shared);
        return imageRepository.findById(imageId).orElse(null);
    }


    // 검색
    public List<Image> findAllBySearchAndMember(String searchTerm, long memberId){
//        Member member = memberService.getMemberByMemberId(memberId)
//        return imageRepository.findAllBySearchAndMemberId(searchTerm, member.getMemberId());
        return imageRepository.findAllBySearchAndMemberId(searchTerm, memberId);
    }

    public List<Image> findAllBySearchAndShared(String searchTerm, boolean shared){
        return imageRepository.findAllBySearchAndShared(searchTerm, shared);
    }

    public void deleteByImageId(long imageId){
        imageRepository.deleteById(imageId);
    }


    public Image findByImageId(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found with id: " + id));
    }


}















































