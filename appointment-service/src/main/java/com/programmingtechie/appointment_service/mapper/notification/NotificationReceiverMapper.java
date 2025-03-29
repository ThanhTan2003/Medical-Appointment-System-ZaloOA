package com.programmingtechie.appointment_service.mapper.notification;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.notification.NotificationReceiverResponse;
import com.programmingtechie.appointment_service.enity.notification.NotificationReceiver;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class NotificationReceiverMapper {
    final NotificationPermissionMapper notificationPermissionMapper;

    public NotificationReceiverResponse toNotificationReceiverResponse(NotificationReceiver notificationReceiver) {
        return NotificationReceiverResponse.builder()
                .id(notificationReceiver.getId())
                .uid(notificationReceiver.getUid())
                .notificationPermissionsResponse(notificationReceiver.getNotificationPermissions().stream()
                        .map(notificationPermissionMapper::toNotificationPermissionResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
