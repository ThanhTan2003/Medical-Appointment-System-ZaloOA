package com.programmingtechie.appointment_service.dto.request.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    private String patientId;

    private String doctorServiceId;

    private String doctorScheduleId;

    private LocalDate appointmentDate;

}
