package com.programmingtechie.appointment_service.dto.request.appointment;

import java.time.LocalDate;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {
    private String zaloUid;

    private String fullName;

    private String gender;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private String identityCard;

    private String address;

    private String notes;
}
