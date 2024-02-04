package com.ImageTrip.image.entity;

import com.ImageTrip.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;
    @Column(nullable = false)
    private String uri; //S3에 저장된 파일 URL
    @Column(columnDefinition="bit(1) default 0")
    private boolean isShared;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
    @CreatedDate
    @Column(updatable = false)
    public LocalDateTime createdDate;
    @LastModifiedDate
    public LocalDateTime updatedDate;
    @Column(nullable = false)
    private String name;    //S3에 저장된 파일 이름
    @Column(nullable = false)
    private double lat;
    @Column(nullable = false)
    private double lon;
    @Column(nullable = false)
    private String addr;
    @Column(nullable = false)
    private long fileSize;  //파일 크기(바이트 단위)
    private String tag;

    @Builder
    public Image(String uri, boolean isShared, Member member, LocalDateTime createdDate, LocalDateTime updatedDate, String name, float lat, float lon, String addr, long fileSize, String tag) {
        this.uri = uri;
        this.isShared = isShared;

        this.member = member;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.addr = addr;
        this.fileSize = fileSize;
        this.tag = tag;
    }

//    public void update(String uri, boolean isShared, LocalDateTime updatedDate, String name, float lat, float lon, long fileSize, String tag) {
//        this.uri = uri;
//        this.isShared = isShared;
//        this.updatedDate = updatedDate;
//        this.name = name;
//        this.lat = lat;
//        this.lon = lon;
//        this.fileSize = fileSize;
//        this.tag = tag;
//    }
}
