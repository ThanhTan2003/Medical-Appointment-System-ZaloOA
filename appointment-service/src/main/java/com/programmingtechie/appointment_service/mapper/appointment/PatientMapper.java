package com.programmingtechie.appointment_service.mapper.appointment;

import com.programmingtechie.appointment_service.enums.IconURLAddress;
import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.appointment.PatientResponse;
import com.programmingtechie.appointment_service.enity.appointment.Patient;

@Component
public class PatientMapper {
    public PatientResponse toPatientResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .zaloUid(patient.getZaloUid())
                .fullName(patient.getFullName())
                .gender(patient.getGender())
                .phoneNumber(patient.getPhoneNumber())
                .dateOfBirth(patient.getDateOfBirth())
                .identityCard(patient.getIdentityCard())
                .address(patient.getAddress())
                .insuranceId(patient.getInsuranceId())
                .image(IconURLAddress.PATIENT.getUrl())
                .build();
    }
}
