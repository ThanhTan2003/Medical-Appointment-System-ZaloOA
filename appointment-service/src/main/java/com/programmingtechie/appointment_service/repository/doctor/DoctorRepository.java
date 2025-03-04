package com.programmingtechie.appointment_service.repository.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.appointment_service.enity.doctor.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, String> {
    @Query(
            "SELECT d FROM Doctor d ORDER BY unaccent(LOWER(split_part(name, ' ', array_length(string_to_array(name, ' '), 1)))) ASC")
    Page<Doctor> getAllDoctor(Pageable pageable);

    boolean existsByPhone(String phone);

    Page<Doctor> findByStatus(Boolean status, Pageable pageable);

    @Query(
            value =
                    "SELECT * FROM public.doctor WHERE "
                            + "unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                            + "unaccent(LOWER(name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                            + "unaccent(LOWER(phone)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                            + "unaccent(LOWER(academic_title)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                            + "unaccent(LOWER(gender)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                            + "ORDER BY unaccent(LOWER(split_part(name, ' ', array_length(string_to_array(name, ' '), 1)))) ASC", // Sort by last name (last word)
            nativeQuery = true)
    Page<Doctor> searchDoctorsWithUnaccent(@Param("keyword") String keyword, Pageable pageable);
}
