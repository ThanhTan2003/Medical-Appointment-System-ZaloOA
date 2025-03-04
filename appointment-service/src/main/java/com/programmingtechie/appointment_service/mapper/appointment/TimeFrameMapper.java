package com.programmingtechie.appointment_service.mapper.appointment;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.appointment.TimeFrameResponse;
import com.programmingtechie.appointment_service.enity.medical.TimeFrame;

@Component
public class TimeFrameMapper {
    public TimeFrameResponse toTimeFrameResponse(TimeFrame timeFrame) {
        return TimeFrameResponse.builder()
                .id(timeFrame.getId())
                .startTime(timeFrame.getStartTime())
                .endTime(timeFrame.getEndTime())
                .session(timeFrame.getSession())
                .build();
    }
}
