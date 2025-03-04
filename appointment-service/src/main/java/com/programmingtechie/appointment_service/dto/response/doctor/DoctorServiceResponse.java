package com.programmingtechie.appointment_service.dto.response.doctor;

import com.programmingtechie.appointment_service.dto.response.medical.ServiceResponse;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorServiceResponse {
    private String id;

    private String doctorId;

    private String serviceId;

    private Double serviceFee;

    private Boolean status;

    private DoctorResponse doctorResponse;

    private ServiceResponse serviceResponse;
}
