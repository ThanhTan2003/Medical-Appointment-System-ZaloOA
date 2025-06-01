package com.programmingtechie.appointment_service.repository.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.appointment_service.enity.doctor.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, String> {
    @Query(
        value = "SELECT d.*, " +
                "unaccent(LOWER(split_part(d.name, ' ', array_length(string_to_array(d.name, ' '), 1)))) as last_name " +
                "FROM doctor d " +
                "ORDER BY last_name ASC",
        nativeQuery = true)
    Page<Doctor> getAllDoctor(Pageable pageable);

    boolean existsByPhone(String phone);

    @Query(value = "SELECT * FROM doctor WHERE status = :status " +
            "ORDER BY unaccent(LOWER(split_part(name, ' ', array_length(string_to_array(name, ' '), 1)))) ASC",
            nativeQuery = true)
    Page<Doctor> findByStatus(Boolean status, Pageable pageable);

    @Query(value = "SELECT d.*, " +
            "unaccent(LOWER(split_part(d.name, ' ', array_length(string_to_array(d.name, ' '), 1)))) as last_name " +
            "FROM doctor d " +
            "WHERE unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(phone)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(gender)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY last_name ASC",
        nativeQuery = true)
    Page<Doctor> searchDoctorsWithUnaccent(@Param("keyword") String keyword, Pageable pageable);

    @Query(
        value = "SELECT DISTINCT d.*, " +
                "unaccent(LOWER(split_part(d.name, ' ', array_length(string_to_array(d.name, ' '), 1)))) as last_name " +
                "FROM doctor d " +
                "JOIN doctor_service ds ON ds.doctor_id = d.id " +
                "JOIN service s ON s.id = ds.service_id " +
                "WHERE (unaccent(LOWER(d.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) " +
                "AND ds.status = true " +
                "AND d.status = true " +
                "AND s.id = :serviceId " +
                "ORDER BY last_name ASC",
        nativeQuery = true)
    Page<Doctor> findDoctorsByServiceId(
        @Param("keyword") String keyword,
        @Param("serviceId") String serviceId,
        Pageable pageable
    );

    @Query(
        value = "SELECT DISTINCT d.*, " +
                "unaccent(LOWER(split_part(d.name, ' ', array_length(string_to_array(d.name, ' '), 1)))) as last_name " +
                "FROM doctor d " +
                "JOIN doctor_service ds ON ds.doctor_id = d.id " +
                "JOIN service s ON s.id = ds.service_id " +
                "JOIN service_category sc ON sc.id = s.service_category_id " +
                "WHERE d.status = :status " +
                "AND sc.id = :serviceCategoryId " +
                "AND (" +
                "   unaccent(LOWER(d.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
                "   unaccent(LOWER(d.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
                "   unaccent(LOWER(d.phone)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
                "   unaccent(LOWER(d.gender)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) " +
                ") " +
                "ORDER BY last_name ASC",
        nativeQuery = true)
    Page<Doctor> searchDoctorsWithStatusAndCategory(
        @Param("keyword") String keyword,
        @Param("status") Boolean status,
        @Param("serviceCategoryId") String serviceCategoryId,
        Pageable pageable
    );

    @Query(
        value = "SELECT DISTINCT d.*, " +
                "unaccent(LOWER(split_part(d.name, ' ', array_length(string_to_array(d.name, ' '), 1)))) as last_name " +
                "FROM doctor d " +
                "WHERE d.status = :status " +
                "AND (" +
                "   unaccent(LOWER(d.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
                "   unaccent(LOWER(d.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
                "   unaccent(LOWER(d.phone)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
                "   unaccent(LOWER(d.gender)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) " +
                ") " +
                "ORDER BY last_name ASC",
        nativeQuery = true)
    Page<Doctor> searchDoctorsWithStatus(
        @Param("keyword") String keyword,
        @Param("status") Boolean status,
        Pageable pageable
    );
}