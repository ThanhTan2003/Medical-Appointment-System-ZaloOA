package com.programmingtechie.appointment_service.mapper.appointment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.programmingtechie.appointment_service.mapper.doctor.DoctorScheduleMapper;
import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.appointment.AppointmentResponse;
import com.programmingtechie.appointment_service.enity.appointment.Appointment;
import com.programmingtechie.appointment_service.mapper.doctor.DoctorServiceMapper;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AppointmentMapper {
    final DoctorScheduleMapper doctorScheduleMapper;
    final DoctorServiceMapper doctorServiceMapper;
    final PatientMapper patientMapper;

    public AppointmentResponse toAppointmentResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .zaloUid(appointment.getZaloUid())
                .patientId(appointment.getPatient().getId())
                .doctorServiceId(appointment.getDoctorService().getId())
                .doctorScheduleId(appointment.getDoctorSchedule().getId())
                .bookingTime(appointment.getBookingTime())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentDateName(getFormattedDateName(appointment.getAppointmentDate()))
                .status(appointment.getStatus())
                .patientResponse(patientMapper.toPatientResponse(appointment.getPatient()))
                .doctorScheduleResponse(doctorScheduleMapper.toDoctorScheduleResponse(appointment.getDoctorSchedule()))
                .doctorServiceResponse(doctorServiceMapper.toDoctorServiceResponse(appointment.getDoctorService()))
                .build();
    }

    public List<AppointmentResponse> toAppointmentResponseList(List<Appointment> appointments) {
        return appointments.stream().map(this::toAppointmentResponse).collect(Collectors.toList());
    }

    // Hàm nhận vào LocalDate và trả về chuỗi định dạng "Tên ngày - ngày/ tháng/năm"
    private String getFormattedDateName(LocalDate date) {
        if (date == null) {
            return "";
        }

        // Định dạng ngày là ngày/tháng/năm
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Lấy tên ngày trong tuần (e.g. Thứ hai)
        String dayOfWeek =
                date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, Locale.forLanguageTag("vi"));

        return dayOfWeek + " - " + formattedDate;
    }
}
