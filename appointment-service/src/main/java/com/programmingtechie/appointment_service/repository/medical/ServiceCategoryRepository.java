package com.programmingtechie.appointment_service.repository.medical;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.doctor.Doctor;
import com.programmingtechie.appointment_service.enity.medical.ServiceCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, String> {
    @Query("SELECT s FROM ServiceCategory s")
    Page<Doctor> getAll(Pageable pageable);

    boolean existsByCategoryName(String categoryName);

    // Tìm giá trị displayOrder lớn nhất
    @Query("SELECT MAX(s.displayOrder) FROM ServiceCategory s")
    Optional<Integer> findMaxDisplayOrder();

    @Query(
            value =
                    "SELECT * FROM public.service_category WHERE "
                            + "(unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                            + "OR unaccent(LOWER(name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) "
                            + "ORDER BY display_order ASC",
            nativeQuery = true)
    Page<ServiceCategory> search(String keyword, Pageable pageable);

    // Lấy danh sách tên ServiceCategory mà bác sĩ cung cấp dịch vụ
    @Query(value = "SELECT sc.name FROM service_category sc " +
            "WHERE sc.id IN (SELECT DISTINCT s.service_category_id FROM service s " +
            "JOIN doctor_service ds ON s.id = ds.service_id " +
            "WHERE ds.doctor_id = :doctorId) " +
            "ORDER BY sc.display_order ASC",
            nativeQuery = true)
    List<String> findServiceCategoryNamesByDoctorId(@Param("doctorId") String doctorId);


    @Query(value = """
    SELECT DISTINCT sc.*, 
    (SELECT COUNT(*) FROM service s 
     WHERE s.service_category_id = sc.id) as quantity 
    FROM service_category sc
    JOIN service s ON s.service_category_id = sc.id
    JOIN doctor_service ds ON ds.service_id = s.id
    WHERE ds.doctor_id = :doctorId
    ORDER BY sc.display_order ASC
    """, nativeQuery = true)
    Page<ServiceCategory> findByDoctorId(
            @Param("doctorId") String doctorId,
            Pageable pageable
    );

}