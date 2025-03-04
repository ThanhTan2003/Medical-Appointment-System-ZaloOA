package com.programmingtechie.appointment_service.mapper.appointment;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.appointment.ClinicResponse;
import com.programmingtechie.appointment_service.enity.appointment.Clinic;

@Component
public class ClinicMapper {
    public ClinicResponse toClinicResponse(Clinic clinic) {
        return ClinicResponse.builder()
                .id(clinic.getId())
                .clinicName(clinic.getClinicName())
                .address(clinic.getAddress())
                .description(clinic.getDescription())
                .supportPhone(clinic.getSupportPhone())
                .build();
    }
}
