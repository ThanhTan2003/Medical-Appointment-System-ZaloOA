package com.programmingtechie.appointment_service.controller.appointment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.request.appointment.TimeFrameRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.appointment.TimeFrameResponse;
import com.programmingtechie.appointment_service.service.appointment.TimeFrameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointment/timeframes")
@RequiredArgsConstructor
public class TimeFrameController {
    final TimeFrameService timeFrameService;

    @PostMapping
    public ResponseEntity<TimeFrameResponse> create(@RequestBody TimeFrameRequest request) {
        return timeFrameService.create(request);
    }

    @GetMapping
    public PageResponse<TimeFrameResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return timeFrameService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeFrameResponse> getById(@PathVariable String id) {
        return timeFrameService.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeFrameResponse> updateById(
            @PathVariable String id, @RequestBody TimeFrameRequest request) {
        return timeFrameService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        return timeFrameService.deleteById(id);
    }
}
