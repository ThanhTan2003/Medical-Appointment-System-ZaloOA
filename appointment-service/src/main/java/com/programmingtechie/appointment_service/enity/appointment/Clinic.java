package com.programmingtechie.appointment_service.enity.appointment;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "clinic")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clinic {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "clinic_name", columnDefinition = "TEXT")
    private String clinicName;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "support_phone", columnDefinition = "TEXT")
    private String supportPhone;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
