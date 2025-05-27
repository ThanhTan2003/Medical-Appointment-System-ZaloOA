package com.programmingtechie.appointment_service.repository.appointment;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.appointment.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    Page<Appointment> findByDoctorServiceIdOrderByAppointmentDateDesc(String doctorServiceId, Pageable pageable);

    Page<Appointment> findByZaloUidOrderByAppointmentDateDesc(String zaloUid, Pageable pageable);

    Page<Appointment> findByAppointmentDateOrderByAppointmentDateDesc(LocalDate appointmentDate, Pageable pageable);

    // Tìm kiếm theo doctorId, appointmentDate và status
    @Query(value = "SELECT a.* FROM appointment a " +
            "WHERE a.doctor_service_id = :doctorId " +
            "AND a.appointment_date = :appointmentDate " +
            "AND (:status = '' OR unaccent(LOWER(a.status)) LIKE unaccent(LOWER(CONCAT('%', :status, '%')))) " +
            "ORDER BY a.time_frame_id ASC, a.booking_time ASC",
            nativeQuery = true)
    Page<Appointment> findByDoctorIdAndAppointmentDateAndStatus(
            String doctorId, LocalDate appointmentDate, String status, Pageable pageable);

    // Tìm kiếm các cuộc hẹn theo patientId và status
    @Query(value = "SELECT a.* FROM appointment a " +
            "WHERE a.patient_id = :patientId " +
            "AND (:status = '' OR unaccent(LOWER(a.status)) LIKE unaccent(LOWER(CONCAT('%', :status, '%')))) " +
            "ORDER BY a.appointment_date DESC",
            nativeQuery = true)
    Page<Appointment> findByPatientIdAndStatus(String patientId, String status, Pageable pageable);

    // Tìm kiếm theo doctorServiceId và status
    @Query(value = "SELECT a.* FROM appointment a " +
            "WHERE a.doctor_service_id = :doctorServiceId " +
            "AND (:status = '' OR unaccent(LOWER(a.status)) LIKE unaccent(LOWER(CONCAT('%', :status, '%')))) " +
            "ORDER BY a.appointment_date DESC",
            nativeQuery = true)
    Page<Appointment> findByDoctorServiceIdAndStatus(String doctorServiceId, String status, Pageable pageable);

    // Tìm kiếm theo zaloUid và status
    @Query(value = "SELECT a.* FROM appointment a " +
            "WHERE a.zalo_uid = :zaloUid " +
            "AND (:status = '' OR unaccent(LOWER(a.status)) LIKE unaccent(LOWER(CONCAT('%', :status, '%')))) " +
            "ORDER BY a.appointment_date DESC",
            nativeQuery = true)
    Page<Appointment> findByZaloUidAndStatus(String zaloUid, String status, Pageable pageable);

    List<Appointment> findByDoctorScheduleIdAndAppointmentDate(String doctorScheduleId, LocalDate appointmentDate);

    // Đếm số lượng cuộc hẹn theo doctorScheduleId và appointmentDate
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctorSchedule.id = :doctorScheduleId AND a.appointmentDate = :appointmentDate")
    long countByDoctorScheduleIdAndAppointmentDate(@Param("doctorScheduleId") String doctorScheduleId, @Param("appointmentDate") LocalDate appointmentDate);

    @Query(value = """
    SELECT a.* 
    FROM appointment a
    JOIN doctor_service ds ON a.doctor_service_id = ds.id
    JOIN doctor d ON ds.doctor_id = d.id
    JOIN service s ON ds.service_id = s.id
    WHERE a.patient_id = :patientId
      AND (:status = '' OR unaccent(LOWER(a.status)) LIKE unaccent(LOWER(CONCAT('%', :status, '%'))))
      AND (
          :keyword = '' 
          OR unaccent(LOWER(a.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))
          OR unaccent(LOWER(d.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))
          OR unaccent(LOWER(s.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))
      )
    ORDER BY a.appointment_date DESC
    """,
            nativeQuery = true)
    Page<Appointment> findByPatientIdAndStatusAndKeyword(String patientId, String status, String keyword, Pageable pageable);

}
