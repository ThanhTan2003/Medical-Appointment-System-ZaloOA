package com.programmingtechie.appointment_service.controller.notification;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.notification.NotificationPermissionResponse;
import com.programmingtechie.appointment_service.dto.response.notification.NotificationReceiverResponse;
import com.programmingtechie.appointment_service.service.notification.NotificationReceiverService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointment/notifications-receivers")
@RequiredArgsConstructor
public class NotificationReceiverController {

    private final NotificationReceiverService notificationReceiverService;

    // Lấy tất cả các quyền trong hệ thống
    @GetMapping("/permissions")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<List<NotificationPermissionResponse>> getAllPermissions() {
        List<NotificationPermissionResponse> permissions = notificationReceiverService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    // Thêm quyền cho người nhận
    @PostMapping("/add-permission")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<NotificationReceiverResponse> addPermission(
            @RequestParam String uid, @RequestParam String permission) {

        NotificationReceiverResponse response = notificationReceiverService.addPermissionToReceiver(uid, permission);
        return ResponseEntity.ok(response);
    }

    // Xóa quyền của người nhận
    @DeleteMapping("/remove-permission")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<String> removePermission(@RequestParam String uid, @RequestParam String permission) {

        notificationReceiverService.removePermissionFromReceiver(uid, permission);
        return ResponseEntity.ok("Permission removed successfully");
    }

    // Lấy danh sách người nhận thông báo phân trang theo quyền
    @GetMapping("/by-permission")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<NotificationReceiverResponse>> getReceiversByPermission(
            @RequestParam String permission,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PageResponse<NotificationReceiverResponse> response =
                notificationReceiverService.getReceiversByPermission(permission, page, size);

        return ResponseEntity.ok(response);
    }

    // Lấy thông tin NotificationReceiver theo UID
    @GetMapping("/{uid}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<NotificationReceiverResponse> getReceiverByUid(@PathVariable String uid) {
        NotificationReceiverResponse response = notificationReceiverService.getReceiverByUid(uid);
        return ResponseEntity.ok(response);
    }
}