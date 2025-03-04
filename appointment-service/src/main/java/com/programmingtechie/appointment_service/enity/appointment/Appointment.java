package com.programmingtechie.appointment_service.enity.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

import com.programmingtechie.appointment_service.enity.doctor.DoctorService;
import com.programmingtechie.appointment_service.enity.medical.TimeFrame;

import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "appointment",
        indexes = {
            @Index(name = "idx_zalo_uid", columnList = "zalo_uid"),
            @Index(name = "idx_patient_id", columnList = "patient_id"),
            @Index(name = "idx_appointment_date", columnList = "appointment_date"),
            @Index(name = "idx_doctor_service_id", columnList = "doctor_service_id")
        })
public class Appointment {

    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String id;

    @Column(name = "zalo_uid", nullable = false)
    private String zaloUid;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_service_id", nullable = false)
    private DoctorService doctorService;

    @ManyToOne
    @JoinColumn(name = "time_frame_id", nullable = false)
    private TimeFrame timeFrame;

    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "status", columnDefinition = "TEXT", nullable = false)
    private String status;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
