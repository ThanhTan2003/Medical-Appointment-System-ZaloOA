package com.programmingtechie.zalo_oa_service.dto.request.appointmentNotifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppointmentInfo {
    String userId;
    String appointmentId;
    String name;
    String serviceName;
    String date;
    String time;
    String roomName;
    String doctorName;
    String orderNumber;
    String status;
}
