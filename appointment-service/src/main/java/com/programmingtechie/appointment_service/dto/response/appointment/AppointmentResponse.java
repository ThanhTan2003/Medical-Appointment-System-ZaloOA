package com.programmingtechie.appointment_service.dto.response.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.programmingtechie.appointment_service.dto.response.doctor.DoctorScheduleResponse;
import com.programmingtechie.appointment_service.dto.response.doctor.DoctorServiceResponse;

import com.programmingtechie.appointment_service.enity.doctor.DoctorSchedule;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private String id;

    private String zaloUid;

    private String patientId;

    private String doctorServiceId;

    private String doctorScheduleId;

    private LocalDateTime bookingTime;

    private LocalDate appointmentDate;

    private String appointmentDateName;

    private String status;

    private PatientResponse patientResponse;

    private DoctorScheduleResponse doctorScheduleResponse;

    private DoctorServiceResponse doctorServiceResponse;
}
