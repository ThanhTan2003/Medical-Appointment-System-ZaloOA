package com.programmingtechie.zalo_oa_service.dto.request.appointmentNotifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppointmentCancelInfo {
    AppointmentInfo appointmentInfo;
    String reason;
}
