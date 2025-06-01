package com.programmingtechie.appointment_service.enums;

public enum RolePermission {
    QUANTRIVIEN_GIAMDOC("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')");

    private final String permission;

    RolePermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
