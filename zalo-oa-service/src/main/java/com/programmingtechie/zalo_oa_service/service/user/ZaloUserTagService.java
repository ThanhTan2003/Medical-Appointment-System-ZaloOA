package com.programmingtechie.zalo_oa_service.service.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.programmingtechie.zalo_oa_service.dto.request.user.TagRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.zalo_oa_service.dto.response.PageResponse;
import com.programmingtechie.zalo_oa_service.dto.response.user.TagResponse;
import com.programmingtechie.zalo_oa_service.dto.response.user.ZaloUserResponse;
import com.programmingtechie.zalo_oa_service.entity.Tag;
import com.programmingtechie.zalo_oa_service.entity.ZaloUser;
import com.programmingtechie.zalo_oa_service.entity.ZaloUserTag;
import com.programmingtechie.zalo_oa_service.mapper.TagMapper;
import com.programmingtechie.zalo_oa_service.repository.user.ZaloUserRepository;
import com.programmingtechie.zalo_oa_service.repository.user.ZaloUserTagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZaloUserTagService {

    private final ZaloUserTagRepository zaloUserTagRepository;
    private final ZaloUserRepository zaloUserRepository;
    private final TagService tagService;

    final TagMapper tagMapper;

    public void addTagForUser(String userId, String tagName) {
        ZaloUser zaloUser = getZaloUserEntityById(userId);
        if(zaloUser == null)
            throw new IllegalArgumentException("Không tìm thấy thông tin người dùng!");
        Tag tag = tagService.getEntityByName(tagName);
        if(tag == null)
        {
            TagRequest tagRequest = TagRequest.builder()
                    .name(tagName)
                    .build();
            tagService.create(tagRequest);
            addTagForUser(userId, tagName);
        }
        if (zaloUserTagRepository.existsByZaloUserIdAndTagName(userId, tagName)) {
            return;
        }
        ZaloUserTag zaloUserTag =
                ZaloUserTag.builder().zaloUser(zaloUser).tag(tag).build();
        zaloUserTagRepository.save(zaloUserTag);
    }
    public ZaloUser getZaloUserEntityById(String id) {
        Optional<ZaloUser> zaloUser = zaloUserRepository.findById(id);
        return zaloUser.orElse(null);
    }

    // Xoá tag của user
    @Transactional
    public void removeTagFromUser(String userId, String tagName) {
        ZaloUser zaloUser = getZaloUserEntityById(userId);
        if(zaloUser == null)
            throw new IllegalArgumentException("Không tìm thấy thông tin người dùng!");
        Tag tag = tagService.getEntityByName(tagName);
        if(tag == null)
            throw new IllegalArgumentException("Không tìm thấy tag với tên " + tagName);
        ZaloUserTag zaloUserTag = zaloUserTagRepository
                .findByZaloUserIdAndTagName(userId, tagName)
                .orElseThrow(() -> new IllegalArgumentException("User không có tag này"));
        zaloUserTagRepository.delete(zaloUserTag);
    }

    // Lấy danh sách tag của user
    public List<TagResponse> getTagsByUserId(String userId) {
        List<Tag> tags = zaloUserTagRepository.findTagsByUserId(userId);
        return tags.stream()
                .map(tagMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Lấy danh sách user theo tag_name (phân trang)
    public PageResponse<ZaloUserResponse> getUsersByTagName(String tagName, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ZaloUser> userPage = zaloUserTagRepository.findUsersByTagName(tagName, pageable);
        List<ZaloUserResponse> userResponses = userPage.getContent().stream()
                .map(user -> ZaloUserResponse.builder()
                        .id(user.getId())
                        .displayName(user.getDisplayName())
                        .avatar(user.getAvatar())
                        .status(user.getStatus())
                        .build())
                .collect(Collectors.toList());

        return new PageResponse<>(userPage.getTotalPages(), page, size, userPage.getTotalElements(), userResponses);
    }

    // Lấy danh sách user theo danh sách tag_names
    public PageResponse<ZaloUserResponse> getUsersByTagNames(List<String> tagNames, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ZaloUser> userPage = zaloUserTagRepository.findUsersByTagNames(tagNames, pageable);
        List<ZaloUserResponse> userResponses = userPage.getContent().stream()
                .map(user -> ZaloUserResponse.builder()
                        .id(user.getId())
                        .displayName(user.getDisplayName())
                        .avatar(user.getAvatar())
                        .status(user.getStatus())
                        .build())
                .collect(Collectors.toList());

        return new PageResponse<>(userPage.getTotalPages(), page, size, userPage.getTotalElements(), userResponses);
    }
}