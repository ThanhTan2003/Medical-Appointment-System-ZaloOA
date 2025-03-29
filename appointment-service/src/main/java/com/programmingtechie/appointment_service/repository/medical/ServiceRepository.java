package com.programmingtechie.appointment_service.repository.medical;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.medical.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {
    @Query("SELECT d FROM Service d")
    Page<Service> getAllService(Pageable pageable);

    boolean existsByServiceName(String serviceName);

    @Query(
            value =
                    "SELECT * FROM public.service WHERE "
                            + "(unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                            + "OR unaccent(LOWER(name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) ",
            nativeQuery = true)
    Page<Service> search(String keyword, Pageable pageable);

    @Query(
            value =
                    "SELECT * FROM public.service WHERE "
                            + "(unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                            + "OR unaccent(LOWER(name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) "
                            + "AND service_category_id = :serviceCategoryId",
            nativeQuery = true)
    Page<Service> search(String keyword, String serviceCategoryId, Pageable pageable);
}
