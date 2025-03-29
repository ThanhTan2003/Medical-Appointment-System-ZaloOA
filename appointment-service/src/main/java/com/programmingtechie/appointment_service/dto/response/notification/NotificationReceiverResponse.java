package com.programmingtechie.appointment_service.dto.response.notification;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationReceiverResponse {
    private String id;
    private String uid;
    private List<NotificationPermissionResponse> notificationPermissionsResponse;
}
