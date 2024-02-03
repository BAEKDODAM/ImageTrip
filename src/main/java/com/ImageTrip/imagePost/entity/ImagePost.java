//package com.ImageTrip.imagePost.entity;
//
//import com.ImageTrip.image.entity.Image;
//import com.ImageTrip.member.entity.Member;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//public class ImagePost {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long imagePostId;
//    @Column(columnDefinition="bit(1) default 0")
//    private boolean Shared;
//    @ManyToOne
//    @JoinColumn(name = "memberId")
//    private Member member;
//    @Column
//    private String tag;
//    @OneToMany(mappedBy = "post")
//    private List<Image> images;
//
//    @Builder
//    public ImagePost(boolean shared, Member member, String tag, List<Image> images) {
//        this.imagePostId = imagePostId;
//        Shared = shared;
//        this.member = member;
//        this.tag = tag;
//        this.images = images;
//    }
//}
