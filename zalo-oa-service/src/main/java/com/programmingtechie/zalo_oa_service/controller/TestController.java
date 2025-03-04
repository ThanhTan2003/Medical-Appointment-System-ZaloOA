package com.programmingtechie.zalo_oa_service.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.zalo_oa_service.dto.request.appointmentNotifier.AppointmentCancelInfo;
import com.programmingtechie.zalo_oa_service.dto.request.appointmentNotifier.AppointmentInfo;
import com.programmingtechie.zalo_oa_service.oa.APIException;
import com.programmingtechie.zalo_oa_service.service.AppointmentNotifierService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/zalo-oa/test")
@RequiredArgsConstructor
public class TestController {
    final AppointmentNotifierService appointmentNotifierService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {

        // Tạo body của response
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false)); // đường dẫn của request

        // Trả về response với mã 500
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/notify-booking-success")
    public ResponseEntity<Void> notifyBookingSuccess() throws APIException {
        AppointmentInfo appointmentInfo = AppointmentInfo.builder()
                .userId("7546191973773392974")
                .appointmentId("LH-YGLJROLQ954TJP9PZKD")
                .name("Trần Thanh Tân")
                .serviceName("Khám sức khỏe tổng quát")
                .date("Thứ 6 - 28/02/2025")
                .time("7:00 (Sáng)")
                .orderNumber("01")
                .roomName("Phòng khám 01")
                .doctorName("BS CKI Đỗ Thiện Minh")
                .status("Chờ xác nhận")
                .build();
        return ResponseEntity.ok(appointmentNotifierService.notifyBookingSuccess(appointmentInfo));
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmAppointment() throws APIException {
        AppointmentInfo appointmentInfo = AppointmentInfo.builder()
                .userId("7546191973773392974")
                .appointmentId("LH-YGLJROLQ954TJP9PZKD")
                .name("Trần Thanh Tân")
                .serviceName("Khám sức khỏe tổng quát")
                .date("Thứ 6 - 28/02/2025")
                .time("7:00 (Sáng)")
                .orderNumber("01")
                .roomName("Phòng khám 01")
                .doctorName("BS CKI Đỗ Thiện Minh")
                .status("Chờ xác nhận")
                .build();
        return ResponseEntity.ok(appointmentNotifierService.confirmAppointment(appointmentInfo));
    }

    @GetMapping("/cancel")
    public ResponseEntity<Void> cancelAppointment() throws APIException {
        AppointmentInfo appointmentInfo = AppointmentInfo.builder()
                .userId("7546191973773392974")
                .appointmentId("LH-YGLJROLQ954TJP9PZKD")
                .name("Trần Thanh Tân")
                .serviceName("Khám sức khỏe tổng quát")
                .date("Thứ 6 - 28/02/2025")
                .time("7:00 (Sáng)")
                .orderNumber("01")
                .roomName("Phòng khám 01")
                .doctorName("BS CKI Đỗ Thiện Minh")
                .status("Chờ xác nhận")
                .build();
        AppointmentCancelInfo appointmentCancelInfo = AppointmentCancelInfo.builder()
                .appointmentInfo(appointmentInfo)
                .reason("Thông tin đăng ký không hợp lệ")
                .build();
        return ResponseEntity.ok(appointmentNotifierService.cancelAppointment(appointmentCancelInfo));
    }

    @GetMapping("/remind")
    public ResponseEntity<Void> remindAppointment() throws APIException {
        AppointmentInfo appointmentInfo = AppointmentInfo.builder()
                .userId("7546191973773392974")
                .appointmentId("LH-YGLJROLQ954TJP9PZKD")
                .name("Trần Thanh Tân")
                .serviceName("Khám sức khỏe tổng quát")
                .date("Thứ 6 - 28/02/2025")
                .time("7:00 (Sáng)")
                .orderNumber("01")
                .roomName("Phòng khám 01")
                .doctorName("BS CKI Đỗ Thiện Minh")
                .status("Chờ xác nhận")
                .build();
        return ResponseEntity.ok(appointmentNotifierService.remindAppointment(appointmentInfo));
    }

    @GetMapping("/thank-you")
    public ResponseEntity<Void> sendThankYouMessage() throws APIException {
        AppointmentInfo appointmentInfo = AppointmentInfo.builder()
                .userId("7546191973773392974")
                .appointmentId("LH-YGLJROLQ954TJP9PZKD")
                .name("Trần Thanh Tân")
                .serviceName("Khám sức khỏe tổng quát")
                .date("Thứ 6 - 28/02/2025")
                .time("7:00 (Sáng)")
                .orderNumber("01")
                .roomName("Phòng khám 01")
                .doctorName("BS CKI Đỗ Thiện Minh")
                .status("Chờ xác nhận")
                .build();
        return ResponseEntity.ok(appointmentNotifierService.sendThankYouMessage(appointmentInfo));
    }
}
