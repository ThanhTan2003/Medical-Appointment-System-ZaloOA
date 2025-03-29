package com.programmingtechie.appointment_service.mapper.appointment;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.appointment.TimeFrameResponse;
import com.programmingtechie.appointment_service.enity.medical.TimeFrame;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeFrameMapper {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    public TimeFrameResponse toTimeFrameResponse(TimeFrame timeFrame) {
        String formattedName = formatTimeFrameName(timeFrame.getStartTime(), timeFrame.getEndTime());
        String formattedFullName = formatTimeFrameFullName(timeFrame.getStartTime(), timeFrame.getEndTime(), timeFrame.getSession());
        return TimeFrameResponse.builder()
                .id(timeFrame.getId())
                .startTime(timeFrame.getStartTime())
                .endTime(timeFrame.getEndTime())
                .session(timeFrame.getSession())
                .name(formattedName)
                .fullName(formattedFullName)
                .build();
    }

    private String formatTimeFrameName(LocalTime startTime, LocalTime endTime) {
        String start = startTime.format(TIME_FORMATTER);
        String end = endTime.format(TIME_FORMATTER);
        return String.format("%s - %s", start, end);
    }

    private String formatTimeFrameFullName(LocalTime startTime, LocalTime endTime, String session) {
        String start = startTime.format(TIME_FORMATTER);
        String end = endTime.format(TIME_FORMATTER);
        return String.format("%s - %s (%s)", start, end, session);
    }
}
