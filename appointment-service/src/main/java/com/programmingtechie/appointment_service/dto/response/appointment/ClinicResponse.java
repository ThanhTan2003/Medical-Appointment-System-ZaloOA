package com.programmingtechie.appointment_service.dto.response.appointment;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicResponse {
    private String id;

    private String clinicName;

    private String address;

    private String description;

    private String supportPhone;
}
