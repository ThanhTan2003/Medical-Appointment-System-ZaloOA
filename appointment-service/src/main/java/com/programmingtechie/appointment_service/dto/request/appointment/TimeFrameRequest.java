package com.programmingtechie.appointment_service.dto.request.appointment;

import java.time.LocalTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeFrameRequest {
    private LocalTime startTime;
    private LocalTime endTime;
}