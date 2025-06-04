package com.programmingtechie.appointment_service.enums;

public enum DoctorScheduleStatus {
    ACTIVE("Nhận đăng ký"),
    INACTIVE("Ngừng đăng ký");

    private final String description;

    DoctorScheduleStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static DoctorScheduleStatus fromBoolean(Boolean status) {
        if (status == null) {
            return null;
        }
        return status ? ACTIVE : INACTIVE;
    }
}
