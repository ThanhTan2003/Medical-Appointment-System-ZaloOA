package com.programmingtechie.appointment_service.dto.request.appointment;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicRequest {
    private String clinicName;

    private String address;

    private String description;

    private String supportPhone;
}
