package com.programmingtechie.appointment_service.dto.request.doctor;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorServiceRequest {
    private String doctorId;

    private String serviceId;

    private Double serviceFee;

    private Boolean status;
}
