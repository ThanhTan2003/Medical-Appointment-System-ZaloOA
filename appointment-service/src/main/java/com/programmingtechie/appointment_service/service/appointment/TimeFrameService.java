package com.programmingtechie.appointment_service.service.appointment;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.programmingtechie.appointment_service.enums.Session;
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
import java.util.Optional;
import java.util.Arrays;
import java.util.Comparator;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TimeFrameService {
    private final TimeFrameRepository timeFrameRepository;
    private final TimeFrameMapper timeFrameMapper;

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
        if (request.getStartTime().equals(request.getEndTime())) {
            throw new IllegalArgumentException("Thời gian bắt đầu không được trùng thời gian kết thúc!");
        }
        // Kiểm tra thời gian có thuộc session nào không
        Session startSession = Session.fromTime(request.getStartTime());
        Session endSession = Session.fromTime(request.getEndTime());
        if (!startSession.equals(endSession)) {
            throw new IllegalArgumentException("Thời gian bắt đầu và kết thúc phải trong cùng một buổi!");
        }
    }


    @Transactional
    public ResponseEntity<TimeFrameResponse> create(TimeFrameRequest request) {
        validateTimeFrameRequest(request);

        Optional<TimeFrame> existingTimeFrame = timeFrameRepository.findOverlappingTimeFrame(
                request.getStartTime(),
                request.getEndTime(),
                true
        );

        if (existingTimeFrame.isPresent()) {
            throw new IllegalArgumentException("Đã tồn tại khung giờ khám chồng chéo với khung giờ này!");
        }

        Session session = Session.fromTime(request.getStartTime());
        TimeFrame timeFrame = TimeFrame.builder()
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .session(session.getDescription())
                .status(true)
                .build();

        timeFrame = timeFrameRepository.save(timeFrame);
        return ResponseEntity.ok(timeFrameMapper.toTimeFrameResponse(timeFrame));
    }

    @Transactional
    public ResponseEntity<TimeFrameResponse> updateById(String id, TimeFrameRequest request) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã giờ khám!");
        }
        validateTimeFrameRequest(request);

        TimeFrame existingTimeFrame = timeFrameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giờ khám có mã " + id + "!"));

        // Kiểm tra xem thời gian mới có bị chồng chéo với khung giờ khác không
        Optional<TimeFrame> overlappingTimeFrame = timeFrameRepository.findOverlappingTimeFrame(
                request.getStartTime(),
                request.getEndTime(),
                true
        );

        if (overlappingTimeFrame.isPresent() && !overlappingTimeFrame.get().getId().equals(id)) {
            throw new IllegalArgumentException("Đã tồn tại khung giờ khám chồng chéo với khung giờ này!");
        }

        Session newSession = Session.fromTime(request.getStartTime());
        existingTimeFrame.setStartTime(request.getStartTime());
        existingTimeFrame.setEndTime(request.getEndTime());
        existingTimeFrame.setSession(newSession.name());

        existingTimeFrame = timeFrameRepository.save(existingTimeFrame);
        return ResponseEntity.ok(timeFrameMapper.toTimeFrameResponse(existingTimeFrame));
    }


    public ResponseEntity<List<TimeFrameResponse>> getAllActiveTimeFrames() {
        List<TimeFrame> timeFrames = timeFrameRepository.findAllActiveOrderBySessionAndTime(true);
        List<TimeFrameResponse> responses = timeFrames.stream()
                .map(timeFrameMapper::toTimeFrameResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    public PageResponse<TimeFrameResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<TimeFrame> pageData = timeFrameRepository.findAll(pageable);

        List<TimeFrameResponse> responses = pageData.getContent().stream()
                .map(timeFrameMapper::toTimeFrameResponse)
                .toList();

        return new PageResponse<>(
                pageData.getTotalPages(),
                page,
                size,
                pageData.getTotalElements(),
                responses
        );
    }

    public ResponseEntity<TimeFrameResponse> getById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã giờ khám!");
        }

        TimeFrame timeFrame = timeFrameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giờ khám có mã " + id + "!"));

        return ResponseEntity.ok(timeFrameMapper.toTimeFrameResponse(timeFrame));
    }

    @Transactional
    public ResponseEntity<String> deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã giờ khám!");
        }

        TimeFrame timeFrame = timeFrameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giờ khám có mã " + id + "!"));

        if (timeFrameRepository.existsByDoctorSchedule(id)) {
            timeFrame.setStatus(false);
            timeFrameRepository.save(timeFrame);
            return ResponseEntity.ok("Giờ khám có mã " + id + " đã được vô hiệu hóa.");
        }

        timeFrameRepository.delete(timeFrame);
        return ResponseEntity.ok("Giờ khám có mã " + id + " đã được xóa thành công.");
    }

    public ResponseEntity<List<String>> getAllSessions() {
        List<String> sessions = Arrays.stream(Session.values())
                .sorted(Comparator.comparingInt(Session::getOrder))
                .map(Session::getDescription)
                .toList();
        return ResponseEntity.ok(sessions);
    }

}