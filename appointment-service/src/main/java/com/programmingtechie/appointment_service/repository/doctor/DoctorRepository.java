package com.programmingtechie.appointment_service.repository.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.appointment_service.enity.doctor.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, String> {
    @Query(value = "SELECT * FROM doctor " +
            "ORDER BY unaccent(LOWER(split_part(name, ' ', array_length(string_to_array(name, ' '), 1)))) ASC",
            nativeQuery = true)
    Page<Doctor> getAllDoctor(Pageable pageable);

    boolean existsByPhone(String phone);

    Page<Doctor> findByStatus(Boolean status, Pageable pageable);

    @Query(
            value =
                    "SELECT * FROM public.doctor WHERE "
                            + "unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                            + "unaccent(LOWER(name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                            + "unaccent(LOWER(phone)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                            + "unaccent(LOWER(gender)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                            + "ORDER BY unaccent(LOWER(split_part(name, ' ', array_length(string_to_array(name, ' '), 1)))) ASC",
            nativeQuery = true)
    Page<Doctor> searchDoctorsWithUnaccent(@Param("keyword") String keyword, Pageable pageable);

    // Tìm bác sĩ theo từ khóa và serviceId, kiểm tra trạng thái của DoctorService và Doctor đều phải true
    @Query(
            value = "SELECT d.* FROM doctor d " +
                    "JOIN doctor_service ds ON ds.doctor_id = d.id " +
                    "JOIN service s ON s.id = ds.service_id " +
                    "WHERE (unaccent(LOWER(d.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) " +
                    "AND ds.status = true " +
                    "AND d.status = true " +
                    "AND s.id = :serviceId",
            nativeQuery = true)
    Page<Doctor> findDoctorsByServiceId(@Param("keyword") String keyword,
                                        @Param("serviceId") String serviceId,
                                        Pageable pageable);
}
