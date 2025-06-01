package com.programmingtechie.identity_service.enums;

import lombok.Getter;

@Getter
public enum DefaultRoles {
    NGUOI_DUNG("NguoiDung", "Người dùng"),
    GIAM_DOC("GiamDoc", "Giám đốc"),
    QUAN_TRI_VIEN("QuanTriVienHeThong", "Quản trị viên hệ thống"),
    BAC_SI("BacSi", "Bác sĩ"),
    NHAN_VIEN_TU_VAN("NhanVienTuVan", "Nhân viên tư vấn"),
    QUAN_LY_LICH_KHAM_BENH("QuanLyLichKhamBenh", "Quản lý lịch khám bệnh");
    private final String id;
    private final String name;

    DefaultRoles(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
