package com.programmingtechie.appointment_service.enums;

public enum DoctorServiceStatus {
    ACTIVE("Nhận khám"),
    INACTIVE("Ngừng khám");

    private final String description;

    DoctorServiceStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static DoctorServiceStatus fromBoolean(Boolean status) {
        if (status == null) {
            return null;
        }
        return status ? ACTIVE : INACTIVE;
    }
}