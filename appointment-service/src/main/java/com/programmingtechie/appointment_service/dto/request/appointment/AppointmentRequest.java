package com.programmingtechie.appointment_service.dto.request.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    private String zaloUid;

    private String patientId;

    private String doctorServiceId;

    private String timeFrameId;

    private LocalDateTime bookingTime;

    private LocalDate appointmentDate;

    private String status;
}
