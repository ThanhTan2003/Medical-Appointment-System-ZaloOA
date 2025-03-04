package com.programmingtechie.appointment_service.repository.appointment;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.appointment.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    Page<Appointment> findByDoctorServiceIdOrderByAppointmentDateDesc(String doctorServiceId, Pageable pageable);

    Page<Appointment> findByZaloUidOrderByAppointmentDateDesc(String zaloUid, Pageable pageable);

    Page<Appointment> findByPatientIdOrderByAppointmentDateDesc(String patientId, Pageable pageable);

    Page<Appointment> findByAppointmentDateOrderByAppointmentDateDesc(LocalDate appointmentDate, Pageable pageable);

    // Tìm kiếm theo doctorId, appointmentDate và status
    @Query("SELECT a FROM Appointment a " + "WHERE a.doctorService.doctor.id = :doctorId "
            + "AND a.appointmentDate = :appointmentDate "
            + "AND (:status = '' OR unaccent(LOWER(a.status)) LIKE unaccent(LOWER(CONCAT('%', :status, '%')))) "
            + "ORDER BY a.timeFrame.id ASC, a.bookingTime ASC")
    Page<Appointment> findByDoctorIdAndAppointmentDateAndStatus(
            String doctorId, LocalDate appointmentDate, String status, Pageable pageable);
}
