package com.programmingtechie.appointment_service.mapper.doctor;

import com.programmingtechie.appointment_service.enums.DoctorScheduleStatus;
import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.doctor.DoctorScheduleResponse;
import com.programmingtechie.appointment_service.enity.doctor.DoctorSchedule;
import com.programmingtechie.appointment_service.mapper.appointment.TimeFrameMapper;

@Component
public class DoctorScheduleMapper {

    private final DoctorMapper doctorMapper;
    private final TimeFrameMapper timeFrameMapper;

    public DoctorScheduleMapper(DoctorMapper doctorMapper, TimeFrameMapper timeFrameMapper) {
        this.doctorMapper = doctorMapper;
        this.timeFrameMapper = timeFrameMapper;
    }

    public DoctorScheduleResponse toDoctorScheduleResponse(DoctorSchedule doctorSchedule) {
        String statusName = doctorSchedule.getStatus() ? DoctorScheduleStatus.ACTIVE.getDescription() : DoctorScheduleStatus.INACTIVE.getDescription();

        return DoctorScheduleResponse.builder()
                .id(doctorSchedule.getId())
                .doctorId(doctorSchedule.getDoctor().getId())
                .timeFrameId(doctorSchedule.getTimeFrame().getId())
                .dayOfWeek(doctorSchedule.getDayOfWeek())
                .maxPatients(doctorSchedule.getMaxPatients())
                .status(doctorSchedule.getStatus())
                .statusName(statusName)
                .roomName(null)
                .doctorResponse(doctorMapper.toDoctorResponse(doctorSchedule.getDoctor()))
                .timeFrameResponse(timeFrameMapper.toTimeFrameResponse(doctorSchedule.getTimeFrame()))
                .build();
    }

}
