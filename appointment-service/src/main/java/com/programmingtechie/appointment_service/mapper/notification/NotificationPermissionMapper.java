package com.programmingtechie.appointment_service.mapper.notification;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.notification.NotificationPermissionResponse;
import com.programmingtechie.appointment_service.enity.notification.NotificationPermission;
import com.programmingtechie.appointment_service.enums.NotificationPermissionType;

@Component
public class NotificationPermissionMapper {

    public NotificationPermissionResponse toNotificationPermissionResponse(
            NotificationPermission notificationPermission) {
        NotificationPermissionType permissionType =
                NotificationPermissionType.fromString(notificationPermission.getPermission());

        return NotificationPermissionResponse.builder()
                .id(notificationPermission.getPermission())
                .name(permissionType != null ? permissionType.getDescription() : "Không rõ")
                .build();
    }
}
