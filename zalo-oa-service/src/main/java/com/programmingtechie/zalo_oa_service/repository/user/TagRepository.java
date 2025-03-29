package com.programmingtechie.zalo_oa_service.repository.user;

import java.util.Optional;

import com.programmingtechie.zalo_oa_service.entity.ZaloUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.zalo_oa_service.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, String> {

    // Kiểm tra xem tag có tồn tại hay không bằng cách tìm kiếm theo tên (không phân biệt hoa thường và dấu)
    @Query(
            value =
                    "SELECT COUNT(*) > 0 FROM tag WHERE unaccent(LOWER(name)) LIKE unaccent(LOWER(CONCAT('%', :name, '%')))",
            nativeQuery = true)
    boolean existsByName(@Param("name") String name);

    // Tìm tag theo name (không phân biệt hoa thường và dấu)
    @Query(
            value = "SELECT * FROM tag WHERE unaccent(LOWER(name)) LIKE unaccent(LOWER(CONCAT('%', :name, '%')))",
            nativeQuery = true)
    Optional<Tag> findByName(@Param("name") String name);

    // Tìm kiếm tag theo id
    Optional<Tag> findById(String id);

    // Lấy tất cả tag với phân trang
    Page<Tag> findAll(Pageable pageable);

    @Query(
            value =
                    "SELECT * FROM public.tag WHERE "
                            + "(unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                            + "OR unaccent(LOWER(name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) "
                            + "ORDER BY unaccent(LOWER(split_part(name, ' ', array_length(string_to_array(name, ' '), 1)))) ASC",
            nativeQuery = true)
    Page<Tag> getTags(@Param("keyword") String keyword, Pageable pageable);
}
