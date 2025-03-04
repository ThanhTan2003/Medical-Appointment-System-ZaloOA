package com.programmingtechie.appointment_service.enums;

public enum DoctorStatus {
    ACTIVE("Đang làm việc"),
    INACTIVE("Ngừng làm việc");

    private final String description;

    DoctorStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static DoctorStatus fromBoolean(Boolean status) {
        if (status == null) {
            return null;
        }
        return status ? ACTIVE : INACTIVE;
    }
}
