package com.programmingtechie.zalo_oa_service.service.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.programmingtechie.zalo_oa_service.repository.user.ZaloUserTagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.zalo_oa_service.dto.request.user.TagRequest;
import com.programmingtechie.zalo_oa_service.dto.response.PageResponse;
import com.programmingtechie.zalo_oa_service.dto.response.user.TagResponse;
import com.programmingtechie.zalo_oa_service.entity.Tag;
import com.programmingtechie.zalo_oa_service.repository.user.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ZaloUserTagRepository zaloUserTagRepository;

    @Transactional
    public TagResponse create(TagRequest request) {
        if (tagRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Tag với tên này đã tồn tại.");
        }
        Tag tag = Tag.builder().name(request.getName()).build();
        tag = tagRepository.save(tag);
        return TagResponse.builder().id(tag.getId()).name(tag.getName()).build();
    }

    @Transactional
    public TagResponse update(String id, TagRequest request) {
        Tag tag = tagRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tag với id " + id));
        tag.setName(request.getName());
        tag = tagRepository.save(tag);
        return TagResponse.builder().id(tag.getId()).name(tag.getName()).build();
    }

    @Transactional
    public void delete(String id) {
        if (!tagRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy tag với id " + id);
        }
        tagRepository.deleteById(id);
    }

    // Kiểm tra xem tag có tồn tại theo name
    public boolean checkTagExists(String name) {
        return tagRepository.existsByName(name);
    }

    // Lấy danh sách các tag phân trang
    public PageResponse<TagResponse> getTags(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size); // JPA bắt đầu từ page 0
        Page<Tag> tagPage = tagRepository.getTags(keyword, pageable);

        // Lấy số lượng người dùng cho mỗi tag
        List<String> tagIds = tagPage.getContent().stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        // Lấy kết quả từ repository và chuyển đổi thành Map<String, Long>
        List<Object[]> userCounts = zaloUserTagRepository.countUsersByTagIds(tagIds);
        Map<String, Long> userCountMap = userCounts.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0], // tagId
                        row -> (Long) row[1]    // userCount
                ));

        // Tạo danh sách TagResponse với quantity
        List<TagResponse> tagResponses = tagPage.getContent().stream()
                .map(tag -> TagResponse.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .quantity(userCountMap.getOrDefault(tag.getId(), 0L).intValue()) // Sử dụng số lượng người dùng
                        .build())
                .collect(Collectors.toList());

        return PageResponse.<TagResponse>builder()
                .totalPages(tagPage.getTotalPages())
                .currentPage(page)
                .pageSize(size)
                .totalElements(tagPage.getTotalElements())
                .data(tagResponses)
                .build();
    }

    // Lấy thông tin tag theo id
    public TagResponse getTagById(String id) {
        Tag tag = tagRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tag với id " + id));
        return TagResponse.builder().id(tag.getId()).name(tag.getName()).build();
    }

    // Lấy thông tin tag theo name
    public TagResponse getTagByName(String name) {
        Tag tag = tagRepository
                .findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tag với tên " + name));
        return TagResponse.builder().id(tag.getId()).name(tag.getName()).build();
    }
    public Tag getEntityByName(String name) {
        Optional<Tag> tag = tagRepository.findByName(name);
        return tag.orElse(null);
    }
}
