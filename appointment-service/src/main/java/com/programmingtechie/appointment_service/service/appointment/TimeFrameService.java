package com.programmingtechie.appointment_service.service.appointment;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.programmingtechie.appointment_service.dto.request.appointment.TimeFrameRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.appointment.TimeFrameResponse;
import com.programmingtechie.appointment_service.enity.medical.TimeFrame;
import com.programmingtechie.appointment_service.mapper.appointment.TimeFrameMapper;
import com.programmingtechie.appointment_service.repository.appointment.TimeFrameRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeFrameService {
    final TimeFrameRepository timeFrameRepository;
    final TimeFrameMapper timeFrameMapper;

    private void validateTimeFrameRequest(TimeFrameRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Khung giờ không được để trống!");
        }
        if (request.getStartTime() == null) {
            throw new IllegalArgumentException("Thời gian bắt đầu không được để trống!");
        }
        if (request.getEndTime() == null) {
            throw new IllegalArgumentException("Thời gian kết thúc không được để trống!");
        }
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException("Thời gian bắt đầu phải trước thời gian kết thúc!");
        }
    }

    public ResponseEntity<TimeFrameResponse> create(TimeFrameRequest request) {
        validateTimeFrameRequest(request);

        if (timeFrameRepository.existsByStartTimeAndEndTime(request.getStartTime(), request.getEndTime())) {
            throw new IllegalArgumentException("Khung giờ này đã tồn tại!");
        }

        TimeFrame timeFrame = TimeFrame.builder()
                .id(UUID.randomUUID().toString())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .session(request.getSession())
                .build();

        timeFrame = timeFrameRepository.save(timeFrame);
        return ResponseEntity.ok(timeFrameMapper.toTimeFrameResponse(timeFrame));
    }

    public PageResponse<TimeFrameResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<TimeFrame> pageData = timeFrameRepository.findAll(pageable);

        List<TimeFrameResponse> responses = pageData.getContent().stream()
                .map(timeFrameMapper::toTimeFrameResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }

    public ResponseEntity<TimeFrameResponse> getById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã khung giờ!");
        }

        return timeFrameRepository
                .findById(id)
                .map(timeFrame -> ResponseEntity.ok(timeFrameMapper.toTimeFrameResponse(timeFrame)))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khung giờ có mã " + id + "!"));
    }

    public ResponseEntity<TimeFrameResponse> updateById(String id, TimeFrameRequest request) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã khung giờ!");
        }
        validateTimeFrameRequest(request);

        TimeFrame existingTimeFrame = timeFrameRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khung giờ có mã " + id + "!"));

        boolean isUpdated = false;

        if (!existingTimeFrame.getStartTime().equals(request.getStartTime())) {
            existingTimeFrame.setStartTime(request.getStartTime());
            isUpdated = true;
        }
        if (!existingTimeFrame.getEndTime().equals(request.getEndTime())) {
            existingTimeFrame.setEndTime(request.getEndTime());
            isUpdated = true;
        }
        if (!existingTimeFrame.getSession().equals(request.getSession())) {
            existingTimeFrame.setSession(request.getSession());
            isUpdated = true;
        }

        if (!isUpdated) {
            return ResponseEntity.ok(timeFrameMapper.toTimeFrameResponse(existingTimeFrame));
        }

        existingTimeFrame = timeFrameRepository.save(existingTimeFrame);
        return ResponseEntity.ok(timeFrameMapper.toTimeFrameResponse(existingTimeFrame));
    }

    public ResponseEntity<String> deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã khung giờ!");
        }

        if (!timeFrameRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy khung giờ có mã " + id + "!");
        }

        timeFrameRepository.deleteById(id);
        return ResponseEntity.ok("Khung giờ có mã " + id + " đã được xóa thành công.");
    }
}
