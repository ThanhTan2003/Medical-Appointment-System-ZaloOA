package com.programmingtechie.appointment_service.dto.response.doctor;

import com.programmingtechie.appointment_service.dto.response.medical.ServiceCategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private String id;

    private String name;

    private String zaloUid;

    private String phone;

    private String gender;

    private String description;

    private Boolean status;

    private String statusName;

    private String image;

    private List<String> nameOfServiceCategory;

    //private List<String> serviceCategories;
}