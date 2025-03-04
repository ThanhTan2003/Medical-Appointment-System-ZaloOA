package com.programmingtechie.appointment_service.enity.doctor;

import java.time.DayOfWeek;
import java.util.UUID;

import jakarta.persistence.*;

import com.programmingtechie.appointment_service.enity.medical.TimeFrame;

import lombok.*;

@Entity
@Table(name = "doctor_schedule")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorSchedule {

    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name = "time_frame_id", nullable = false)
    private TimeFrame timeFrame;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "max_patients", nullable = false)
    private Integer maxPatients;

    @Column(name = "status", nullable = false)
    private Boolean status; // Trạng thái: true = Hoạt động, false = Tạm ngừng

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
