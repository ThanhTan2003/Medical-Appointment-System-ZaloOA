package com.programmingtechie.appointment_service.enums;

import java.time.LocalTime;

public enum Session {
    MORNING("Sáng", 1, LocalTime.of(0, 0), LocalTime.of(12, 0)),
    AFTERNOON("Chiều", 2, LocalTime.of(12, 0), LocalTime.of(17, 59)),
    EVENING("Tối", 3, LocalTime.of(17, 59), LocalTime.of(23, 59));

    private final String description;
    private final int order;
    private final LocalTime startTime;
    private final LocalTime endTime;

    Session(String description, int order, LocalTime startTime, LocalTime endTime) {
        this.description = description;
        this.order = order;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public int getOrder() {
        return order;
    }

    public static Session fromTime(LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Thời gian không được để trống!");
        }

        for (Session session : values()) {
            if ((time.equals(session.startTime) || time.isAfter(session.startTime)) 
                && time.isBefore(session.endTime)) {
                return session;
            }
        }
        throw new IllegalArgumentException("Thời gian không hợp lệ!");
    }
}