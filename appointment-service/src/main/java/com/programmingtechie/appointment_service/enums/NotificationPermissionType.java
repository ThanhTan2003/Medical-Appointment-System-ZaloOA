package com.programmingtechie.appointment_service.enums;

public enum NotificationPermissionType { // Đổi tên enum
    RECEIVE_MESSAGE_NOTIFICATION("Nhận thông báo khi có tin nhắn mới"),
    RECEIVE_APPOINTMENT_NOTIFICATION("Nhận thông báo khi có lịch hẹn mới");

    private final String description;

    NotificationPermissionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static NotificationPermissionType fromString(String permission) {
        for (NotificationPermissionType permissionEnum : NotificationPermissionType.values()) {
            if (permissionEnum.name().equalsIgnoreCase(permission)) {
                return permissionEnum;
            }
        }
        return null;
    }
}
