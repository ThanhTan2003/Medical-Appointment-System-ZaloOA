package com.programmingtechie.appointment_service.service.notification;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.notification.NotificationPermissionResponse;
import com.programmingtechie.appointment_service.dto.response.notification.NotificationReceiverResponse;
import com.programmingtechie.appointment_service.enity.notification.NotificationPermission;
import com.programmingtechie.appointment_service.enity.notification.NotificationReceiver;
import com.programmingtechie.appointment_service.enums.NotificationPermissionType;
import com.programmingtechie.appointment_service.mapper.notification.NotificationReceiverMapper;
import com.programmingtechie.appointment_service.repository.notification.NotificationPermissionRepository;
import com.programmingtechie.appointment_service.repository.notification.NotificationReceiverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationReceiverService {
    private final NotificationReceiverRepository notificationReceiverRepository;
    private final NotificationPermissionRepository notificationPermissionRepository;
    private final NotificationReceiverMapper notificationReceiverMapper;

    // Lấy tất cả các quyền trong hệ thống (NotificationPermissionType)
    public List<NotificationPermissionResponse> getAllPermissions() {
        return List.of(NotificationPermissionType.values()).stream()
                .map(permission -> NotificationPermissionResponse.builder()
                        .id(permission.name())
                        .name(permission.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    // Thêm quyền cho người nhận
    public NotificationReceiverResponse addPermissionToReceiver(String uid, String permission) {
        NotificationReceiver receiver = notificationReceiverRepository
                .findByUid(uid)
                .orElseGet(() -> {
                    NotificationReceiver newReceiver = new NotificationReceiver();
                    newReceiver.setUid(uid);
                    return notificationReceiverRepository.save(newReceiver);
                });

        NotificationPermissionType permissionType = NotificationPermissionType.fromString(permission);
        if (permissionType != null) {
            boolean permissionExists = receiver.getNotificationPermissions().stream()
                    .anyMatch(
                            permissionEntity -> permissionEntity.getPermission().equals(permissionType.name()));
            if (!permissionExists) {
                NotificationPermission notificationPermission = new NotificationPermission();
                notificationPermission.setPermission(permissionType.name());
                notificationPermission.setNotificationReceiver(receiver);
                notificationPermissionRepository.save(notificationPermission); // Lưu quyền mới vào DB
            }
        }

        return notificationReceiverMapper.toNotificationReceiverResponse(receiver);
    }

    // Xóa quyền của người nhận
    public void removePermissionFromReceiver(String uid, String permission) {
        NotificationReceiver receiver = notificationReceiverRepository
                .findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        NotificationPermission permissionToRemove =
                notificationPermissionRepository.findByNotificationReceiverAndPermission(receiver, permission);
        if (permissionToRemove != null) {
            notificationPermissionRepository.delete(permissionToRemove);
            if (receiver.getNotificationPermissions().isEmpty()) {
                notificationReceiverRepository.delete(receiver);
            }
        }
    }

    // Lấy danh sách NotificationReceiver phân trang theo quyền
    public PageResponse<NotificationReceiverResponse> getReceiversByPermission(String permission, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<NotificationReceiver> pageData = (permission.isEmpty())
                ? notificationReceiverRepository.findAll(pageable)
                : notificationReceiverRepository.findByNotificationPermissions_Permission(permission, pageable);

        List<NotificationReceiverResponse> response = pageData.getContent().stream()
                .map(receiver -> NotificationReceiverResponse.builder()
                        .id(receiver.getId())
                        .uid(receiver.getUid())
                        .notificationPermissionsResponse(receiver.getNotificationPermissions().stream()
                                .map(permissionEntity -> NotificationPermissionResponse.builder()
                                        .id(permissionEntity.getPermission())
                                        .name(NotificationPermissionType.valueOf(permissionEntity.getPermission())
                                                .getDescription())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), response);
    }

    // Lấy thông tin NotificationReceiver theo uid
    public NotificationReceiverResponse getReceiverByUid(String uid) {
        NotificationReceiver receiver = notificationReceiverRepository
                .findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return NotificationReceiverResponse.builder()
                .id(receiver.getId())
                .uid(receiver.getUid())
                .notificationPermissionsResponse(receiver.getNotificationPermissions().stream()
                        .map(permissionEntity -> NotificationPermissionResponse.builder()
                                .id(permissionEntity.getPermission())
                                .name(NotificationPermissionType.valueOf(permissionEntity.getPermission())
                                        .getDescription())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
