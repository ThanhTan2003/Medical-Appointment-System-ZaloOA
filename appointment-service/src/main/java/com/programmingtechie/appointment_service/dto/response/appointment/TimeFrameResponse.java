package com.programmingtechie.appointment_service.dto.response.appointment;

import java.time.LocalTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeFrameResponse {
    private String id;

    private LocalTime startTime;

    private LocalTime endTime;

    private String session;
}
