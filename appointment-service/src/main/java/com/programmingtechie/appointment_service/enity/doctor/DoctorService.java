package com.programmingtechie.appointment_service.enity.doctor;

import java.util.UUID;

import jakarta.persistence.*;

import com.programmingtechie.appointment_service.enity.medical.Service;

import lombok.*;

@Entity
@Table(name = "doctor_service")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorService {

    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "service_fee", nullable = false)
    private Double serviceFee;

    @Column(name = "status", nullable = false)
    private Boolean status; // Trạng thái: true = Nhận đăng ký, false = Ngừng hoạt động

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
