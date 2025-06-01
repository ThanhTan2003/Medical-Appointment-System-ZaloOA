package com.programmingtechie.appointment_service.repository.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.doctor.DoctorService;

@Repository
public interface DoctorServiceRepository extends JpaRepository<DoctorService, String> {
    Page<DoctorService> findByDoctorIdOrderByServiceFeeDesc(String doctorId, Pageable pageable);

    Page<DoctorService> findByServiceIdOrderByServiceFeeDesc(String serviceId, Pageable pageable);

    Page<DoctorService> findByStatus(Boolean status, Pageable pageable);

    // Tìm kiếm bác sĩ theo từ khóa và serviceId, kiểm tra trạng thái DoctorService và Doctor đều là true
    @Query(
            value = "SELECT ds.* FROM doctor_service ds " +
                    "JOIN doctor d ON ds.doctor_id = d.id " +
                    "JOIN service s ON ds.service_id = s.id " +
                    "WHERE (unaccent(LOWER(d.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) " +
                    "AND ds.status = true " +
                    "AND d.status = true " +
                    "AND s.id = :serviceId",
            nativeQuery = true)
    Page<DoctorService> findDoctorsByServiceId(@Param("keyword") String keyword,
                                               @Param("serviceId") String serviceId,
                                               Pageable pageable);

    // Tìm kiếm bác sĩ theo từ khóa và doctorId, kiểm tra trạng thái DoctorService và Doctor đều là true
    @Query(
            value = "SELECT ds.* FROM doctor_service ds " +
                    "JOIN doctor d ON ds.doctor_id = d.id " +
                    "JOIN service s ON ds.service_id = s.id " +
                    "WHERE (unaccent(LOWER(s.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) " +
                    "AND ds.status = true " +
                    "AND d.status = true " +
                    "AND d.id = :doctorId",
            nativeQuery = true)
    Page<DoctorService> findDoctorsByDoctorId(@Param("keyword") String keyword,
                                               @Param("doctorId") String doctorId,
                                               Pageable pageable);

    @Query(
        value = "SELECT COUNT(DISTINCT ds.doctor_id) " +
                "FROM doctor_service ds " +
                "JOIN doctor d ON ds.doctor_id = d.id " +
                "WHERE ds.service_id = :serviceId " +
                "AND ds.status = true " +
                "AND d.status = true",
        nativeQuery = true)
    Long countActiveDoctoringByServiceId(@Param("serviceId") String serviceId);




    @Query(value = """
    SELECT DISTINCT ds.* FROM doctor_service ds
    JOIN service s ON s.id = ds.service_id
    WHERE ds.doctor_id = :doctorId
    AND (:keyword = '' OR 
        unaccent(LOWER(s.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR
        unaccent(LOWER(s.description)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))
    )
    AND (:status IS NULL OR ds.status = :status)
    AND (:serviceCategoryId IS NULL OR :serviceCategoryId = '' OR s.service_category_id = :serviceCategoryId)
    ORDER BY ds.price DESC
    """, nativeQuery = true)
    Page<DoctorService> searchByDoctorIdWithFilters(
            @Param("keyword") String keyword,
            @Param("doctorId") String doctorId,
            @Param("status") Boolean status,
            @Param("serviceCategoryId") String serviceCategoryId,
            Pageable pageable
    );
}