package com.programmingtechie.appointment_service.dto.response.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.programmingtechie.appointment_service.dto.response.doctor.DoctorServiceResponse;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private String id;

    private String zaloUid;

    private String patientId;

    private String patientName;

    private String doctorServiceId;

    private String timeFrameId;

    private LocalDateTime bookingTime;

    private LocalDate appointmentDate;

    private String status;

    private TimeFrameResponse timeFrameResponse;

    private DoctorServiceResponse doctorServiceResponse;
}
