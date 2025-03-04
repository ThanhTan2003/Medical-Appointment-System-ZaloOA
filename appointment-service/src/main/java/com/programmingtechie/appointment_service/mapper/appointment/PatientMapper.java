package com.programmingtechie.appointment_service.mapper.appointment;

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
                .notes(patient.getNotes())
                .build();
    }
}
