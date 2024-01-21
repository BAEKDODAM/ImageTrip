package com.ImageTrip.image.service;

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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public List<String> saveImages(List<MultipartFile> images, String tag, boolean shared, long memberId){
        //Member member = memberService.getMemberByMemberId(memberId);
        Member member = new Member();

        return images.stream().map(img -> {
            Image image = new Image();
            String failedUpload = null;
            UUID uuid = UUID.randomUUID();
//            String type = img.getContentType();   // 굳이 필요하나?
            String name = uuid.toString() + SEPERATOR + img.getOriginalFilename();

            try (InputStream inputStream = img.getInputStream()) {
                Metadata metadata;
                try{// 이미지 파일의 위치(lat, lon)데이터 가져오기
                    metadata = ImageMetadataReader.readMetadata(inputStream);
                    GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
                    if (gpsDirectory != null) {
                        GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                        if (geoLocation != null) {
                            double latitude = geoLocation.getLatitude();
                            double longitude = geoLocation.getLongitude();
//                            String imageSavePath = amazonS3.getUrl(bucketName, name).toString(); //데이터베이스에 저장할 이미지가 저장된 주소

//                            image.setUri(imageSavePath);
                            image.setMember(member);
                            image.setName(name);
                            image.setTag(tag);
                            image.setLat(latitude);
                            image.setLon(longitude);
                            image.setShared(shared);

                            try {
                                // 위치 데이터(위도, 경도)를 api를 이용하여 주소로 변환
                                image.setAddr(geoCodingService.convertAddrKakao(latitude, longitude));
                            } catch (Exception e) {
                                // 예외 처리 로직
                                // 예를 들어, 로그를 남기거나, 기본 주소를 설정하는 등의 처리를 할 수 있습니다.
                                image.setAddr(null);
                            }

                            imageRepository.save(image);
                        }
                    } else{ //위치 데이터가 없을 때
                        failedUpload = img.getOriginalFilename();
                    }
                } catch (ImageProcessingException e) {
                    // 이미지 처리 중 발생한 예외 처리
                    failedUpload = img.getOriginalFilename();
                }

            } catch (IOException e) {
                // 예외 처리
                failedUpload = img.getOriginalFilename();
            }

            return failedUpload;
        }).filter(Objects::nonNull)  // null이 아닌 요소만 필터링
                .collect(Collectors.toList());  // 최종 결과를 리스트로 수집(모든 파일 업로드 성공 시 빈 리스트 반환)
    }

}

















































