package com.programmingtechie.appointment_service.enums;

public enum AppointmentStatus {
    PENDING_APPROVAL("Chờ phê duyệt"),
    APPROVED("Đã phê duyệt"),
    CANCELLED("Đã huỷ"),
    WAITING_FOR_EXAM("Chờ khám"),
    EXAMINED("Đã khám");

    private final String description;

    AppointmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AppointmentStatus fromString(String status) {
        for (AppointmentStatus appointmentStatus : AppointmentStatus.values()) {
            if (appointmentStatus.name().equalsIgnoreCase(status)) {
                return appointmentStatus;
            }
        }
        return null;
    }
}
