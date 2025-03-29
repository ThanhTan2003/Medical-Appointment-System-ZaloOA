package com.programmingtechie.zalo_oa_service.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.zalo_oa_service.dto.request.user.TagRequest;
import com.programmingtechie.zalo_oa_service.dto.response.PageResponse;
import com.programmingtechie.zalo_oa_service.dto.response.user.TagResponse;
import com.programmingtechie.zalo_oa_service.service.user.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/zalo-oa/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    // Thêm tag mới
    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestBody TagRequest request) {
        TagResponse response = tagService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Cập nhật tag theo id
    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable String id, @RequestBody TagRequest request) {
        TagResponse response = tagService.update(id, request);
        return ResponseEntity.ok(response);
    }

    // Xoá tag theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable String id) {
        tagService.delete(id);
        return ResponseEntity.ok("Tag với id " + id + " đã bị xoá.");
    }

    // Kiểm tra sự tồn tại của tag theo name
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkTagExists(@RequestParam String name) {
        boolean exists = tagService.checkTagExists(name);
        return ResponseEntity.ok(exists);
    }

    // Lấy danh sách tag phân trang
    @GetMapping
    public ResponseEntity<PageResponse<TagResponse>> getTags(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageResponse<TagResponse> response = tagService.getTags(keyword, page, size);
        return ResponseEntity.ok(response);
    }

    // Lấy tag theo id
    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getTagById(@PathVariable String id) {
        TagResponse response = tagService.getTagById(id);
        return ResponseEntity.ok(response);
    }

    // Lấy tag theo name
    @GetMapping("/by-name")
    public ResponseEntity<TagResponse> getTagByName(@RequestParam String name) {
        TagResponse response = tagService.getTagByName(name);
        return ResponseEntity.ok(response);
    }
}
