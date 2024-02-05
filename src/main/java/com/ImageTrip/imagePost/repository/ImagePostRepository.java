//package com.ImageTrip.imagePost.repository;
//
//import com.ImageTrip.image.entity.Image;
//import com.ImageTrip.imagePost.entity.ImagePost;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ImagePostRepository extends JpaRepository<ImagePost, Long> {
////    ImagePost findByImagePostId(long imagePostId);
//
//    // 태그 또는 사용자이름이 주어진 문자열을 포함하는 이미지를 검색
//    @Query("SELECT i FROM ImagePost i WHERE i.tag LIKE %:searchTerm% OR i.member.name LIKE %:searchTerm%")
//    List<Image> findByTagOrMemberUsername(@Param("searchTerm") String searchTerm);
//
//    List<Image> findByShared(boolean Shared);
//
//    @Modifying
//    @Query("UPDATE ImagePost i SET i.tag = :tag, i.Shared = :Shared WHERE i.imagePostId = :imagePostId")
//    int updateImagePostTagAndShared(@Param("imagePostId") Long imagePostId, @Param("tag") String tag, @Param("Shared") boolean Shared);
//
//}
