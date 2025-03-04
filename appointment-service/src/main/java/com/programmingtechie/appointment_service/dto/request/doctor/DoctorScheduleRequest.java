package com.programmingtechie.appointment_service.dto.request.doctor;

import java.time.DayOfWeek;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleRequest {
    private String doctorId;

    private String timeFrameId;

    private DayOfWeek dayOfWeek;

    private Integer maxPatients;

    private Boolean status;
}
