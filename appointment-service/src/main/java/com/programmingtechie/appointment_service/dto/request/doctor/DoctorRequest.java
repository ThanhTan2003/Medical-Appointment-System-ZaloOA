package com.programmingtechie.appointment_service.dto.request.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequest {
    private String name;

    private String zaloUid;

    private String academicTitle;

    private String phone;

    private String gender;

    private String description;

    private Boolean status;
}
