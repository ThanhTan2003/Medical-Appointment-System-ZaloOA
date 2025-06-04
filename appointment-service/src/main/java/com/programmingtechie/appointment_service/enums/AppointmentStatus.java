package com.programmingtechie.appointment_service.enums;

public enum AppointmentStatus {
    PENDING_CONFIRMATION("Chờ xác nhận"),
    CONFIRMED("Đã xác nhận"),
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
            if (appointmentStatus.getDescription().equalsIgnoreCase(status)) {
                return appointmentStatus;
            }
        }
        return null;
    }
}