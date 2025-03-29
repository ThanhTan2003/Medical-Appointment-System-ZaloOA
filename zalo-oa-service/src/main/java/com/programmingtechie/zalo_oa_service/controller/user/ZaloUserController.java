package com.programmingtechie.zalo_oa_service.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.zalo_oa_service.dto.request.user.ZaloUserRequest;
import com.programmingtechie.zalo_oa_service.dto.response.PageResponse;
import com.programmingtechie.zalo_oa_service.dto.response.user.ZaloUserResponse;
import com.programmingtechie.zalo_oa_service.service.user.ZaloUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/zalo-oa/user")
@RequiredArgsConstructor
public class ZaloUserController {
    private final ZaloUserService zaloUserService;

    @GetMapping("/{id}")
    public ResponseEntity<ZaloUserResponse> getById(@PathVariable String id) {
        return zaloUserService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ZaloUserResponse> create(@RequestBody ZaloUserRequest request) {
        return zaloUserService.create(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZaloUserResponse> update(@PathVariable String id, @RequestBody ZaloUserRequest request) {
        return zaloUserService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        return zaloUserService.deleteById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ZaloUserResponse>> searchUsers(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String tagId,  // Thay đổi từ status thành tagId
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        // Nếu tagId không rỗng, tìm kiếm theo tagId
        return ResponseEntity.ok(zaloUserService.searchUsers(keyword, tagId, page, size));
    }

}
