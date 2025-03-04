package com.programmingtechie.zalo_oa_service.enums;

import lombok.Getter;

@Getter
public enum NavigationURLAddress {
    INFORMATION("https://www.google.com/"),
    APPOINTMENT_PAYMENT("https://www.google.com/"),
    CANCEL_APPOINTMENT("https://www.google.com/"),
    CUSTOMER_SUPPORT("https://www.google.com/"),
    RESCHEDULE_APPOINTMENT("https://www.google.com/"),
    REVIEW_APPOINTMENT("https://www.google.com/");

    private final String url;

    NavigationURLAddress(String url) {
        this.url = url;
    }
}
