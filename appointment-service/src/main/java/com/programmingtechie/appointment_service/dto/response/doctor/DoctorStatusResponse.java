package com.programmingtechie.appointment_service.dto.response.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorStatusResponse {
    private Boolean status;
    private String description;
}