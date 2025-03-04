package com.programmingtechie.appointment_service.mapper.doctor;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.doctor.DoctorResponse;
import com.programmingtechie.appointment_service.enity.doctor.Doctor;
import com.programmingtechie.appointment_service.enums.DoctorStatus;

@Component
public class DoctorMapper {
    public DoctorResponse toDoctorResponse(Doctor doctor) {
        DoctorResponse doctorResponse = DoctorResponse.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .status(doctor.getStatus())
                .build();

        DoctorStatus statusEnum = DoctorStatus.fromBoolean(doctor.getStatus());
        if (statusEnum != null) {
            doctorResponse.setStatusName(statusEnum.getDescription());
        }

        return doctorResponse;
    }
}
