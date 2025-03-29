package com.programmingtechie.zalo_oa_service.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.zalo_oa_service.entity.Tag;
import com.programmingtechie.zalo_oa_service.entity.ZaloUser;
import com.programmingtechie.zalo_oa_service.entity.ZaloUserTag;

public interface ZaloUserTagRepository extends JpaRepository<ZaloUserTag, String> {

    // Kiểm tra xem một user có tag này hay không
    @Query(
            value =
                    "SELECT COUNT(*) > 0 FROM zalo_user_tag zut " + "JOIN tag t ON zut.tag_id = t.id "
                            + "WHERE zut.zalo_user_id = :zaloUserId AND unaccent(LOWER(t.name)) LIKE unaccent(LOWER(CONCAT('%', :tagName, '%')))",
            nativeQuery = true)
    boolean existsByZaloUserIdAndTagName(@Param("zaloUserId") String zaloUserId, @Param("tagName") String tagName);

    // Kiểm tra xem một user có tag này hay không
    @Query(value = "SELECT * FROM zalo_user_tag zut " +
            "JOIN zalo_user zu ON zut.zalo_user_id = zu.id " +
            "JOIN tag t ON zut.tag_id = t.id " +
            "WHERE zu.id = :zaloUserId AND unaccent(LOWER(t.name)) LIKE unaccent(LOWER(CONCAT('%', :tagName, '%')))",
            nativeQuery = true)
    Optional<ZaloUserTag> findByZaloUserIdAndTagName(@Param("zaloUserId") String zaloUserId, @Param("tagName") String tagName);



    // Lấy tất cả các tag của user
    @Query("SELECT ut.tag FROM ZaloUserTag ut WHERE ut.zaloUser.id = :userId")
    List<Tag> findTagsByUserId(String userId);

    // Lấy tất cả các user có tag_name (phân trang)
    @Query(
            value = "SELECT ut.zaloUser FROM ZaloUserTag ut " + "JOIN tag t ON ut.tag.id = t.id "
                    + "WHERE unaccent(LOWER(t.name)) LIKE unaccent(LOWER(CONCAT('%', :tagName, '%')))",
            nativeQuery = true)
    Page<ZaloUser> findUsersByTagName(@Param("tagName") String tagName, Pageable pageable);

    // Lấy tất cả các user có các tag trong danh sách tag_names (phân trang)
    @Query(
            value = "SELECT DISTINCT ut.zaloUser FROM ZaloUserTag ut " + "JOIN tag t ON ut.tag.id = t.id "
                    + "WHERE unaccent(LOWER(t.name)) IN :tagNames",
            nativeQuery = true)
    Page<ZaloUser> findUsersByTagNames(@Param("tagNames") List<String> tagNames, Pageable pageable);

    @Query(
            value = "SELECT t.id AS tagId, COUNT(DISTINCT ut.zalo_user_id) AS userCount " +
                    "FROM zalo_user_tag ut " +
                    "JOIN tag t ON ut.tag_id = t.id " +
                    "WHERE t.id IN :tagIds " +
                    "GROUP BY t.id",
            nativeQuery = true
    )
    List<Object[]> countUsersByTagIds(@Param("tagIds") List<String> tagIds);



}
