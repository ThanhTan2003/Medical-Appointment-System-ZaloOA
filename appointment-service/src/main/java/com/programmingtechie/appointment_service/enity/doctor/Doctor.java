package com.programmingtechie.appointment_service.enity.doctor;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String id;

    @Column(name = "name", columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(name = "zalo_uid")
    private String zaloUid;

    @Column(name = "phone", columnDefinition = "TEXT", nullable = false, unique = true)
    private String phone;

    @Column(name = "gender", columnDefinition = "TEXT")
    private String gender;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", nullable = false)
    private Boolean status; // Tình trạng (true = Đang làm việc, false = Ngừng làm việc)

    @Column(name = "image")
    private String image;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
