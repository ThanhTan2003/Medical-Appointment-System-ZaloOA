package com.programmingtechie.appointment_service.dto.response.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationPermissionResponse {
    String id;
    String name;
}
