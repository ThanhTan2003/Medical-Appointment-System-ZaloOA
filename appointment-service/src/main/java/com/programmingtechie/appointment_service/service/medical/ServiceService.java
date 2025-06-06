package com.programmingtechie.appointment_service.service.medical;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.programmingtechie.appointment_service.dto.request.medical.ServiceRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.medical.ServiceResponse;
import com.programmingtechie.appointment_service.enity.medical.ServiceCategory;
import com.programmingtechie.appointment_service.mapper.medical.ServiceMapper;
import com.programmingtechie.appointment_service.repository.medical.ServiceCategoryRepository;
import com.programmingtechie.appointment_service.repository.medical.ServiceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceService {
    final ServiceRepository serviceRepository;
    final ServiceCategoryRepository serviceCategoryRepository;
    final ServiceMapper serviceMapper;

    private void validateServiceRequest(ServiceRequest serviceRequest) {
        if (serviceRequest == null || serviceRequest.getServiceName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên dịch vụ không được để trống!");
        }
        if (serviceRequest.getServiceCategoryId() == null
                || serviceRequest.getServiceCategoryId().trim().isEmpty()) {
            throw new IllegalArgumentException("Danh mục dịch vụ không được để trống!");
        }
        if (serviceRequest.getStatus() == null) {
            throw new IllegalArgumentException("Trạng thái hoạt động không được để trống!");
        }
    }

    public PageResponse<ServiceResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<com.programmingtechie.appointment_service.enity.medical.Service> pageData =
                serviceRepository.findAll(pageable);

        List<ServiceResponse> serviceResponses = pageData.getContent().stream()
                .map(serviceMapper::toServiceResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceResponses)
                .build();
    }

    public ResponseEntity<ServiceResponse> getById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã dịch vụ!");
        }

        return serviceRepository
                .findById(id)
                .map(service -> ResponseEntity.ok(serviceMapper.toServiceResponse(service)))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ có mã " + id + "!"));
    }

    public ResponseEntity<ServiceResponse> create(ServiceRequest serviceRequest) {
        validateServiceRequest(serviceRequest);

        if (serviceRepository.existsByServiceName(serviceRequest.getServiceName())) {
            throw new IllegalArgumentException("Tên dịch vụ đã tồn tại!");
        }

        ServiceCategory serviceCategory = serviceCategoryRepository
                .findById(serviceRequest.getServiceCategoryId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy danh mục dịch vụ có mã " + serviceRequest.getServiceCategoryId() + "!"));

        com.programmingtechie.appointment_service.enity.medical.Service service =
                com.programmingtechie.appointment_service.enity.medical.Service.builder()
                        .id(UUID.randomUUID().toString())
                        .serviceName(serviceRequest.getServiceName())
                        .description(serviceRequest.getDescription())
                        .price(serviceRequest.getPrice())
                        .serviceCategory(serviceCategory)
                        .status(serviceRequest.getStatus())
                        .build();

        service = serviceRepository.save(service);
        return ResponseEntity.ok(serviceMapper.toServiceResponse(service));
    }

    public ResponseEntity<ServiceResponse> updateById(String id, ServiceRequest serviceRequest) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã dịch vụ!");
        }
        validateServiceRequest(serviceRequest);

        com.programmingtechie.appointment_service.enity.medical.Service existingService = serviceRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ có mã " + id + "!"));

        ServiceCategory serviceCategory = serviceCategoryRepository
                .findById(serviceRequest.getServiceCategoryId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy danh mục dịch vụ có mã " + serviceRequest.getServiceCategoryId() + "!"));

        boolean isUpdated = false;

        if (!existingService.getServiceName().equals(serviceRequest.getServiceName())) {
            existingService.setServiceName(serviceRequest.getServiceName());
            isUpdated = true;
        }
        if (!existingService.getDescription().equals(serviceRequest.getDescription())) {
            existingService.setDescription(serviceRequest.getDescription());
            isUpdated = true;
        }
        if (!existingService.getPrice().equals(serviceRequest.getPrice())) {
            existingService.setPrice(serviceRequest.getPrice());
            isUpdated = true;
        }
        if (!existingService.getServiceCategory().equals(serviceCategory)) {
            existingService.setServiceCategory(serviceCategory);
            isUpdated = true;
        }
        if (!existingService.getStatus().equals(serviceRequest.getStatus())) {
            existingService.setStatus(serviceRequest.getStatus());
            isUpdated = true;
        }

        if (!isUpdated) {
            return ResponseEntity.ok(serviceMapper.toServiceResponse(existingService));
        }

        existingService = serviceRepository.save(existingService);
        return ResponseEntity.ok(serviceMapper.toServiceResponse(existingService));
    }

    public ResponseEntity<String> deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã dịch vụ!");
        }

        if (!serviceRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy dịch vụ có mã " + id + "!");
        }

        serviceRepository.deleteById(id);
        return ResponseEntity.ok("Dịch vụ có mã " + id + " đã được xóa thành công.");
    }

    public PageResponse<ServiceResponse> search(String keyword, String serviceCategoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<com.programmingtechie.appointment_service.enity.medical.Service> pageData;
        if(Objects.equals(serviceCategoryId, "") || serviceCategoryId.isEmpty())
            pageData = serviceRepository.search(keyword, pageable);
        else
            pageData = serviceRepository.search(keyword, serviceCategoryId, pageable);

        List<ServiceResponse> serviceResponses = pageData.getContent().stream()
                .map(serviceMapper::toServiceResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceResponses)
                .build();
    }


    @Transactional(readOnly = true)
    public PageResponse<ServiceResponse> searchServicesNotRegisteredByDoctor(
            String keyword,
            String doctorId,
            String serviceCategoryId,
            int page,
            int size) {
        // Validate input
        if (doctorId == null || doctorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor ID không được để trống");
        }

        // Sanitize input
        keyword = keyword != null ? keyword.trim() : "";
        serviceCategoryId = serviceCategoryId != null ? serviceCategoryId.trim() : "";

        PageRequest pageRequest = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.ASC, "name")
        );

        Page<com.programmingtechie.appointment_service.enity.medical.Service> servicePage = serviceRepository.searchServiceNotRegisteredByDoctor(
                keyword,
                doctorId,
                serviceCategoryId,
                pageRequest
        );

        List<ServiceResponse> serviceResponses = servicePage.getContent().stream()
                .map(serviceMapper::toServiceResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(servicePage.getTotalPages())
                .totalElements(servicePage.getTotalElements())
                .data(serviceResponses)
                .build();
    }
}
