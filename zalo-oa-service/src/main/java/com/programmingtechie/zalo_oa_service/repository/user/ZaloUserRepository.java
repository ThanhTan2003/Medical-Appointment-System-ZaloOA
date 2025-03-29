package com.programmingtechie.zalo_oa_service.repository.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.zalo_oa_service.entity.ZaloUser;

public interface ZaloUserRepository extends JpaRepository<ZaloUser, String> {

    // Tìm kiếm theo từ khóa (id hoặc display_name) và trạng thái
    @Query(
            value =
                    "SELECT * FROM public.zalo_user WHERE "
                            + "(unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                            + "OR unaccent(LOWER(display_name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) "
                            + "AND ( :status IS NULL OR status = :status) "
                            + "ORDER BY unaccent(LOWER(split_part(display_name, ' ', array_length(string_to_array(display_name, ' '), 1)))) ASC",
            nativeQuery = true)
    Page<ZaloUser> findByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") Boolean status, Pageable pageable);

    @Query(
            value = "SELECT DISTINCT z.id, z.display_name, z.avatar, z.status, " +
                    "unaccent(LOWER(split_part(z.display_name, ' ', array_length(string_to_array(z.display_name, ' '), 1)))) AS display_name_sort " +  // Thêm cột này vào SELECT
                    "FROM public.zalo_user z " +
                    "JOIN public.zalo_user_tag zut ON z.id = zut.zalo_user_id " +
                    "JOIN public.tag t ON zut.tag_id = t.id " +
                    "WHERE (unaccent(LOWER(z.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) " +
                    "OR unaccent(LOWER(z.display_name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) " +
                    "AND t.id = :tagId " +
                    "ORDER BY display_name_sort ASC",  // Sắp xếp theo cột mới
            nativeQuery = true
    )
    Page<ZaloUser> findByKeywordAndTag(@Param("keyword") String keyword, @Param("tagId") String tagId, Pageable pageable);


    // Tìm kiếm theo trạng thái
    @Query("SELECT z FROM ZaloUser z WHERE z.status = :status")
    Page<ZaloUser> findByStatus(String status, Pageable pageable);

    // Tìm kiếm theo từ khóa (id hoặc display_name) mà không cần trạng thái
    @Query(
            value =
                    "SELECT * FROM public.zalo_user WHERE "
                            + "(unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                            + "OR unaccent(LOWER(display_name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) "
                            + "ORDER BY unaccent(LOWER(split_part(display_name, ' ', array_length(string_to_array(display_name, ' '), 1)))) ASC", // Sắp xếp theo từ cuối cùng trong display_name
            nativeQuery = true)
    Page<ZaloUser> findByKeyword(String keyword, Pageable pageable);

    // Tìm kiếm tất cả ZaloUser có ít nhất một tag trong danh sách tags (dùng native query)
    @Query(
            value = "SELECT DISTINCT z.* FROM zalo_user z " + "JOIN zalo_user_tag zut ON zut.zalo_user_id = z.id "
                    + "JOIN tag t ON zut.tag_id = t.id "
                    + "WHERE t.name IN :tagNames ",
            nativeQuery = true)
    Page<ZaloUser> findUsersByTags(@Param("tagNames") List<String> tagNames, Pageable pageable);
}
