package com.programmingtechie.appointment_service.dto.response.doctor;

import java.time.DayOfWeek;

import com.programmingtechie.appointment_service.dto.response.appointment.TimeFrameResponse;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleResponse {
    private String id;

    private String doctorId;

    private String timeFrameId;

    private DayOfWeek dayOfWeek;

    private Integer maxPatients;

    private Boolean status;

    private String statusName;

    private String roomName;

    private DoctorResponse doctorResponse;

    private TimeFrameResponse timeFrameResponse;
}
