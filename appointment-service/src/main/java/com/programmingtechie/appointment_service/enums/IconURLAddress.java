package com.programmingtechie.appointment_service.enums;

import lombok.Getter;

@Getter
public enum IconURLAddress {
    DEFAULT_MEDICAL("https://res.cloudinary.com/dzss0q8a7/image/upload/v1742541996/DefaultMedical_wajrqg.png"),
    PATIENT("https://res.cloudinary.com/dzss0q8a7/image/upload/v1742550116/Patient_xoyqpq.png");

    private final String url;

    IconURLAddress(String url) {
        this.url = url;
    }
}
