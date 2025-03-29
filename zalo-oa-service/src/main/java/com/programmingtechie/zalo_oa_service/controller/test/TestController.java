package com.programmingtechie.zalo_oa_service.controller.test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.zalo_oa_service.dto.request.appointmentNotifier.AppointmentCancelInfo;
import com.programmingtechie.zalo_oa_service.dto.request.appointmentNotifier.AppointmentInfo;
import com.programmingtechie.zalo_oa_service.dto.response.PageResponse;
import com.programmingtechie.zalo_oa_service.dto.response.user.ZaloUserProfileResponse;
import com.programmingtechie.zalo_oa_service.oa.APIException;
import com.programmingtechie.zalo_oa_service.service.sendMessage.AppointmentNotifierService;
import com.programmingtechie.zalo_oa_service.service.user.ZaloUserService;
import com.programmingtechie.zalo_oa_service.utils.zaloUser.ZaloUserUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/zalo-oa/test")
@RequiredArgsConstructor
public class TestController {
    final AppointmentNotifierService appointmentNotifierService;
    final ZaloUserUtil zaloUserUtil;
    final ZaloUserService zaloUserService;

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

    // Xử lý exception APIException
    @ExceptionHandler(APIException.class)
    public ResponseEntity<Map<String, Object>> handleAPIException(APIException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "API Error");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @GetMapping("/notify-booking-success")
    public ResponseEntity<Void> notifyBookingSuccess() throws APIException {
        AppointmentInfo appointmentInfo = AppointmentInfo.builder()
                .userId("7546191973773392974")
                .appointmentId("LH-28032025AS5FN")
                .name("Trần Thanh Tân")
                .serviceName("Khám sức khỏe tổng quát")
                .date("Thứ 6 - 28/03/2025")
                .time("7:00 (Sáng)")
                .orderNumber("01")
                .roomName("Phòng khám 01")
                .doctorName("BS CKI. Nguyễn Tiến Đức")
                .status("Chờ xác nhận")
                .build();
        return ResponseEntity.ok(appointmentNotifierService.notifyBookingSuccess(appointmentInfo));
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmAppointment() throws APIException {
        AppointmentInfo appointmentInfo = AppointmentInfo.builder()
                .userId("7546191973773392974")
                .appointmentId("LH-28032025AS5FN")
                .name("Trần Thanh Tân")
                .serviceName("Khám sức khỏe tổng quát")
                .date("Thứ 6 - 28/03/2025")
                .time("7:00 (Sáng)")
                .orderNumber("01")
                .roomName("Phòng khám 01")
                .doctorName("BS CKI. Nguyễn Tiến Đức")
                .status("Đã xác nhận")
                .build();
        return ResponseEntity.ok(appointmentNotifierService.confirmAppointment(appointmentInfo));
    }

    @GetMapping("/cancel")
    public ResponseEntity<Void> cancelAppointment() throws APIException {
        AppointmentInfo appointmentInfo = AppointmentInfo.builder()
                .userId("7546191973773392974")
                .appointmentId("LH-28032025AS5FN")
                .name("Trần Thanh Tân")
                .serviceName("Khám sức khỏe tổng quát")
                .date("Thứ 6 - 28/03/2025")
                .time("7:00 (Sáng)")
                .orderNumber("01")
                .roomName("Phòng khám 01")
                .doctorName("BS CKI. Nguyễn Tiến Đức")
                .status("Đã hủy")
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
                .appointmentId("LH-28032025AS5FN")
                .name("Trần Thanh Tân")
                .serviceName("Khám sức khỏe tổng quát")
                .date("Thứ 6 - 28/03/2025")
                .time("7:00 (Sáng)")
                .orderNumber("01")
                .roomName("Phòng khám 01")
                .doctorName("BS CKI. Nguyễn Tiến Đức")
                .status("Chờ khám")
                .build();
        return ResponseEntity.ok(appointmentNotifierService.remindAppointment(appointmentInfo));
    }

    @GetMapping("/thank-you")
    public ResponseEntity<Void> sendThankYouMessage() throws APIException {
        AppointmentInfo appointmentInfo = AppointmentInfo.builder()
                .userId("7546191973773392974")
                .appointmentId("LH-28032025AS5FN")
                .name("Trần Thanh Tân")
                .serviceName("Khám sức khỏe tổng quát")
                .date("Thứ 6 - 28/03/2025")
                .time("7:00 (Sáng)")
                .orderNumber("01")
                .roomName("Phòng khám 01")
                .doctorName("BS CKI. Nguyễn Tiến Đức")
                .status("Đã khám")
                .build();
        return ResponseEntity.ok(appointmentNotifierService.sendThankYouMessage(appointmentInfo));
    }

    @GetMapping("/followers")
    public ResponseEntity<PageResponse<String>> getFollowers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(zaloUserUtil.getListFollower(page, size));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ZaloUserProfileResponse> getUserProfile(@PathVariable String userId) {
        return ResponseEntity.ok(zaloUserUtil.getUserProfile(userId));
    }
    // Endpoint đồng bộ tất cả người dùng từ Zalo
    @GetMapping("/sync")
    public ResponseEntity<String> syncAllZaloUsers() {
        // Gọi phương thức đồng bộ dữ liệu
        return zaloUserService.syncAllZaloUsersToDatabase();
    }
}
