package com.programmingtechie.zalo_oa_service.enums;

import lombok.Getter;

@Getter
public enum IconURLAddress {
    BANNER("https://res.cloudinary.com/dzss0q8a7/image/upload/v1739780800/banner_bktlnn.jpg"),
    INFORMATION("https://res.cloudinary.com/dzss0q8a7/image/upload/v1739781174/information_1_uqrbpb.png"),
    ATM_CARD("https://res.cloudinary.com/dzss0q8a7/image/upload/v1739781174/atm-card_1_x0hyl2.png"),
    DOCUMENT_CANCEL("https://res.cloudinary.com/dzss0q8a7/image/upload/v1739782285/document_htdiva.png"),
    CUSTOMER_SUPPORT("https://res.cloudinary.com/dzss0q8a7/image/upload/v1739781174/customer-service_1_ywjgpw.png"),
    RESCHEDULE_APPOINTMENT("https://res.cloudinary.com/dzss0q8a7/image/upload/v1739789033/appointment_xkeeti.png"),
    REVIEW_APPOINTMENT("https://res.cloudinary.com/dzss0q8a7/image/upload/v1740332966/review_bkvumy.png");

    private final String url;

    IconURLAddress(String url) {
        this.url = url;
    }
}
