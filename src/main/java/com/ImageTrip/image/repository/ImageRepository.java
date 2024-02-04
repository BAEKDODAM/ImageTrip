package com.ImageTrip.image.repository;

import com.ImageTrip.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
//    List<Image> findByTag(String tag);

    // 태그 또는 사용자이름이 주어진 문자열을 포함하는 이미지를 검색
    @Query("SELECT i FROM Image i WHERE i.tag LIKE %:searchTerm% OR i.member.name LIKE %:searchTerm%")
    List<Image> findByTagOrMemberUsername(@Param("searchTerm") String searchTerm);

    List<Image> findByIsShared(boolean isShared);

    @Modifying
    @Query("UPDATE Image i SET i.tag = :tag, i.isShared = :isShared WHERE i.imageId = :imageId")
    int updateImageTagAndIsShared(@Param("imageId") Long imageId, @Param("tag") String tag, @Param("isShared") boolean isShared);

}
