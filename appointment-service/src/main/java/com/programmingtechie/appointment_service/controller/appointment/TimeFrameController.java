package com.programmingtechie.appointment_service.controller.appointment;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.request.appointment.TimeFrameRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.appointment.TimeFrameResponse;
import com.programmingtechie.appointment_service.service.appointment.TimeFrameService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment/timeframes")
@RequiredArgsConstructor
public class TimeFrameController {
    private final TimeFrameService timeFrameService;

    @PostMapping
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<TimeFrameResponse> create(@Valid @RequestBody TimeFrameRequest request) {
        return timeFrameService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<TimeFrameResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return timeFrameService.getAll(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<TimeFrameResponse> getById(@PathVariable String id) {
        return timeFrameService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<TimeFrameResponse> updateById(
            @PathVariable String id, @RequestBody TimeFrameRequest request) {
        return timeFrameService.updateById(id, request);
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<List<TimeFrameResponse>> getAllActiveTimeFrames() {
        return timeFrameService.getAllActiveTimeFrames();
    }

    @GetMapping("/sessions")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<List<String>> getAllSessions() {
        return timeFrameService.getAllSessions();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        return timeFrameService.deleteById(id);
    }
}