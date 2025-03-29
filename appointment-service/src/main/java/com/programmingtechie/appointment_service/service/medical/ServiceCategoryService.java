package com.programmingtechie.appointment_service.service.medical;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.programmingtechie.appointment_service.dto.request.medical.ServiceCategoryRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.medical.ServiceCategoryResponse;
import com.programmingtechie.appointment_service.enity.medical.ServiceCategory;
import com.programmingtechie.appointment_service.mapper.medical.ServiceCategoryMapper;
import com.programmingtechie.appointment_service.repository.medical.ServiceCategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceCategoryService {
    final ServiceCategoryRepository serviceCategoryRepository;
    final ServiceCategoryMapper serviceCategoryMapper;

    private void validateServiceCategoryRequest(ServiceCategoryRequest serviceCategoryRequest) {
        if (serviceCategoryRequest == null
                || serviceCategoryRequest.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên danh mục dịch vụ không được để trống!");
        }
    }

    public PageResponse<ServiceCategoryResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ServiceCategory> pageData = serviceCategoryRepository.findAll(pageable);

        List<ServiceCategoryResponse> serviceCategoryResponses = pageData.getContent().stream()
                .map(serviceCategoryMapper::toServiceCategoryResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceCategoryResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceCategoryResponses)
                .build();
    }

    public ResponseEntity<ServiceCategoryResponse> getById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã danh mục dịch vụ!");
        }

        return serviceCategoryRepository
                .findById(id)
                .map(serviceCategory ->
                        ResponseEntity.ok(serviceCategoryMapper.toServiceCategoryResponse(serviceCategory)))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục dịch vụ có mã " + id + "!"));
    }

    public ResponseEntity<ServiceCategoryResponse> create(ServiceCategoryRequest serviceCategoryRequest) {
        validateServiceCategoryRequest(serviceCategoryRequest);

        if (serviceCategoryRepository.existsByCategoryName(serviceCategoryRequest.getCategoryName())) {
            throw new IllegalArgumentException("Tên danh mục dịch vụ đã tồn tại!");
        }

        Integer order = serviceCategoryRepository.findMaxDisplayOrder().get();

        ServiceCategory serviceCategory = ServiceCategory.builder()
                .id(UUID.randomUUID().toString())
                .categoryName(serviceCategoryRequest.getCategoryName())
                .description((serviceCategoryRequest.getDescription()))
                .displayOrder(order+1)
                .build();

        serviceCategory = serviceCategoryRepository.save(serviceCategory);
        return ResponseEntity.ok(serviceCategoryMapper.toServiceCategoryResponse(serviceCategory));
    }

    public ResponseEntity<ServiceCategoryResponse> updateById(
            String id, ServiceCategoryRequest serviceCategoryRequest) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã danh mục dịch vụ!");
        }
        validateServiceCategoryRequest(serviceCategoryRequest);

        ServiceCategory existingServiceCategory = serviceCategoryRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục dịch vụ có mã " + id + "!"));

        boolean isUpdated = false;

        if (!existingServiceCategory.getCategoryName().equals(serviceCategoryRequest.getCategoryName())) {
            existingServiceCategory.setCategoryName(serviceCategoryRequest.getCategoryName());
            isUpdated = true;
        }

        if (!isUpdated) {
            return ResponseEntity.ok(serviceCategoryMapper.toServiceCategoryResponse(existingServiceCategory));
        }

        existingServiceCategory = serviceCategoryRepository.save(existingServiceCategory);
        return ResponseEntity.ok(serviceCategoryMapper.toServiceCategoryResponse(existingServiceCategory));
    }

    public ResponseEntity<String> deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã danh mục dịch vụ!");
        }

        if (!serviceCategoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy danh mục dịch vụ có mã " + id + "!");
        }

        serviceCategoryRepository.deleteById(id);
        return ResponseEntity.ok("Danh mục dịch vụ có mã " + id + " đã được xóa thành công.");
    }

    public PageResponse<ServiceCategoryResponse> search(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ServiceCategory> pageData = serviceCategoryRepository.search(keyword, pageable);

        List<ServiceCategoryResponse> serviceCategoryResponses = pageData.getContent().stream()
                .map(serviceCategoryMapper::toServiceCategoryResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceCategoryResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceCategoryResponses)
                .build();
    }
}
