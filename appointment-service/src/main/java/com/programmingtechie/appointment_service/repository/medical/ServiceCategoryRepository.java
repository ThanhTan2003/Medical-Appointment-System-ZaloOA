package com.programmingtechie.appointment_service.repository.medical;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.doctor.Doctor;
import com.programmingtechie.appointment_service.enity.medical.ServiceCategory;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, String> {
    @Query("SELECT d FROM Doctor d")
    Page<Doctor> getAllDoctor(Pageable pageable);

    boolean existsByCategoryName(String categoryName);
}
