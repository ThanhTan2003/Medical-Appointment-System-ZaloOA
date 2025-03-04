package com.programmingtechie.appointment_service.mapper.appointment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.appointment.AppointmentResponse;
import com.programmingtechie.appointment_service.enity.appointment.Appointment;
import com.programmingtechie.appointment_service.mapper.doctor.DoctorServiceMapper;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AppointmentMapper {
    final TimeFrameMapper timeFrameMapper;
    DoctorServiceMapper doctorServiceMapper;

    public AppointmentResponse toAppointmentResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .zaloUid(appointment.getZaloUid())
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getFullName())
                .doctorServiceId(appointment.getDoctorService().getId())
                .timeFrameId(appointment.getTimeFrame().getId())
                .bookingTime(appointment.getBookingTime())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus())
                .timeFrameResponse(timeFrameMapper.toTimeFrameResponse(appointment.getTimeFrame()))
                .doctorServiceResponse(doctorServiceMapper.toDoctorServiceResponse(appointment.getDoctorService()))
                .build();
    }

    public List<AppointmentResponse> toAppointmentResponseList(List<Appointment> appointments) {
        return appointments.stream().map(this::toAppointmentResponse).collect(Collectors.toList());
    }
}
