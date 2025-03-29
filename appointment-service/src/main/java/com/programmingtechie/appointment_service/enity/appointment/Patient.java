package com.programmingtechie.appointment_service.enity.appointment;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "patient_profile",
        indexes = {
            @Index(name = "idx_zalo_uid", columnList = "zalo_uid"),
            @Index(name = "idx_phone_number", columnList = "phone_number"),
            @Index(name = "idx_identity_card", columnList = "identity_card")
        })
public class Patient {

    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String id;

    @Column(name = "zalo_uid", nullable = false)
    private String zaloUid;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "identity_card", nullable = false)
    private String identityCard;

    @Column(name = "insurance_id")
    private String insuranceId;

    @Column(name = "address")
    private String address;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}