package com.programmingtechie.appointment_service.repository.medical;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.medical.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {
    @Query("SELECT d FROM Service d")
    Page<Service> getAllService(Pageable pageable);

    boolean existsByServiceName(String serviceName);

    @Query(
            value =
                    "SELECT * FROM public.service WHERE "
                            + "(unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                            + "OR unaccent(LOWER(name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) ",
            nativeQuery = true)
    Page<Service> search(String keyword, Pageable pageable);

    @Query(
            value =
                    "SELECT * FROM public.service WHERE "
                            + "(unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                            + "OR unaccent(LOWER(name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) "
                            + "AND service_category_id = :serviceCategoryId",
            nativeQuery = true)
    Page<Service> search(String keyword, String serviceCategoryId, Pageable pageable);



    // Tìm kiếm dịch vụ theo từ khóa và trạng thái của bác sĩ
    @Query(
            value = "SELECT DISTINCT s.* FROM service s " +
                    "JOIN doctor_service ds ON ds.service_id = s.id " +
                    "WHERE ds.doctor_id = :doctorId " +
                    "AND (:status IS NULL OR ds.status = :status) " +
                    "AND (unaccent(LOWER(s.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) " +
                    "    OR unaccent(LOWER(s.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) " +
                    "ORDER BY s.name ASC",
            nativeQuery = true)
    Page<Service> searchByDoctor(
            @Param("keyword") String keyword,
            @Param("doctorId") String doctorId,
            @Param("status") Boolean status,
            Pageable pageable
    );



    @Query(value = """
    SELECT s.* FROM service s
    LEFT JOIN doctor_service ds ON s.id = ds.service_id AND ds.doctor_id = :doctorId
    WHERE ds.id IS NULL 
    AND s.status = true
    AND (:keyword = '' OR 
        unaccent(LOWER(s.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR
        unaccent(LOWER(s.description)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))
    )
    AND (:serviceCategoryId IS NULL OR :serviceCategoryId = '' OR s.service_category_id = :serviceCategoryId)
    ORDER BY s.name ASC
    """, nativeQuery = true)
    @QueryHints({
            @QueryHint(name = org.hibernate.annotations.QueryHints.FETCH_SIZE, value = "50"),
            @QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true")
    })
    Page<Service> searchServiceNotRegisteredByDoctor(
            @Param("keyword") String keyword,
            @Param("doctorId") String doctorId,
            @Param("serviceCategoryId") String serviceCategoryId,
            Pageable pageable
    );
}
