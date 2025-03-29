package com.programmingtechie.zalo_oa_service.controller.user;

import java.util.List;

import com.programmingtechie.zalo_oa_service.dto.response.user.TagResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.zalo_oa_service.dto.response.PageResponse;
import com.programmingtechie.zalo_oa_service.dto.response.user.ZaloUserResponse;
import com.programmingtechie.zalo_oa_service.service.user.ZaloUserTagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/zalo-oa/zalo-user-tag")
@RequiredArgsConstructor
public class ZaloUserTagController {

    private final ZaloUserTagService zaloUserTagService;

    // Thêm tag cho user
    @PostMapping("/add")
    public ResponseEntity<String> addTagForUser(@RequestParam String userId, @RequestParam String tagName) {
        zaloUserTagService.addTagForUser(userId, tagName); // Chuyển yêu cầu cho Service xử lý
        return ResponseEntity.ok("Tag đã được thêm cho user.");
    }

    // Xoá tag của user
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeTagFromUser(@RequestParam String userId, @RequestParam String tagName) {
        zaloUserTagService.removeTagFromUser(userId, tagName);
        return ResponseEntity.ok("Tag đã được xoá khỏi user.");
    }

    // Lấy danh sách tag của user
    @GetMapping("/tags")
    public ResponseEntity<List<TagResponse>> getTagsByUserId(@RequestParam String userId) {
        List<TagResponse> tags = zaloUserTagService.getTagsByUserId(userId);
        return ResponseEntity.ok(tags);
    }

    // Lấy danh sách user theo tag_name
    @GetMapping("/users-by-tag")
    public ResponseEntity<PageResponse<ZaloUserResponse>> getUsersByTagName(
            @RequestParam String tagName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PageResponse<ZaloUserResponse> response =
                zaloUserTagService.getUsersByTagName(tagName, page, size);
        return ResponseEntity.ok(response);
    }

    // Lấy danh sách user theo danh sách tag_names
    @GetMapping("/users-by-tags")
    public ResponseEntity<PageResponse<ZaloUserResponse>> getUsersByTagNames(
            @RequestParam List<String> tagNames,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PageResponse<ZaloUserResponse> response =
                zaloUserTagService.getUsersByTagNames(tagNames, page, size);
        return ResponseEntity.ok(response);
    }
}
