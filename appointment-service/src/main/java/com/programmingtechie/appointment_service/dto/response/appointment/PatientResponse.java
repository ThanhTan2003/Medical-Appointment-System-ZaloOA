package com.programmingtechie.appointment_service.dto.response.appointment;

import java.time.LocalDate;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private String id;

    private String zaloUid;

    private String fullName;

    private String gender;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private String identityCard;

    private String address;

    private String notes;
}
