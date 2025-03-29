package com.programmingtechie.appointment_service.repository.appointment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.appointment.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByIdentityCard(String identityCard);

    // Tìm danh sách patient theo zaloUid và keyword
    @Query(
            value = "SELECT * FROM patient_profile p " +
                    "WHERE (:zaloUid IS NULL OR p.zalo_uid = :zaloUid) " +
                    "AND ( " +
                    "(unaccent(LOWER(p.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) " +
                    "OR (unaccent(LOWER(p.full_name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) " +
                    "OR (unaccent(LOWER(p.phone_number)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) " +
                    "OR (unaccent(LOWER(p.identity_card)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) " +
                    "OR (unaccent(LOWER(p.insurance_id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) " +
                    ") " +
                    "ORDER BY p.full_name ASC",
            nativeQuery = true
    )
    Page<Patient> findByZaloUidAndKeyword(@Param("zaloUid") String zaloUid,
                                          @Param("keyword") String keyword,
                                          Pageable pageable);


    boolean existsByInsuranceId(String insuranceId);

    boolean existsByInsuranceIdAndZaloUid(String insuranceId, String zaloUid);

    boolean existsByIdentityCardAndZaloUid(String identityCard, String zaloUid);
}
