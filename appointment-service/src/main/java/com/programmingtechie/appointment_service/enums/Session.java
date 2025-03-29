package com.programmingtechie.appointment_service.enums;

public enum Session {
    MORNING("Sáng", 1),
    AFTERNOON("Chiều", 2),
    EVENING("Tối", 3);

    private final String description;
    private final int order;

    Session(String description, int order) {
        this.description = description;
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public int getOrder() {
        return order;
    }

    public static Session fromString(String session) {
        for (Session sessionEnum : Session.values()) {
            if (sessionEnum.name().equalsIgnoreCase(session)) {
                return sessionEnum;
            }
        }
        return null;
    }
}
