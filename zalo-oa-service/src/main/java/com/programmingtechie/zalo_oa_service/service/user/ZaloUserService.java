package com.programmingtechie.zalo_oa_service.service.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.programmingtechie.zalo_oa_service.dto.request.user.ZaloUserRequest;
import com.programmingtechie.zalo_oa_service.dto.response.PageResponse;
import com.programmingtechie.zalo_oa_service.dto.response.user.ZaloUserProfileResponse;
import com.programmingtechie.zalo_oa_service.dto.response.user.ZaloUserResponse;
import com.programmingtechie.zalo_oa_service.entity.ZaloUser;
import com.programmingtechie.zalo_oa_service.mapper.ZaloUserMapper;
import com.programmingtechie.zalo_oa_service.repository.user.ZaloUserRepository;
import com.programmingtechie.zalo_oa_service.utils.zaloUser.ZaloUserUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZaloUserService {
    private final ZaloUserRepository zaloUserRepository;
    private final ZaloUserMapper zaloUserMapper;
    private final ZaloUserUtil zaloUserUtil;

    private void validateZaloUserRequest(ZaloUserRequest request) {
        if (request == null
                || request.getDisplayName() == null
                || request.getDisplayName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên hiển thị không được để trống!");
        }
    }

    public ResponseEntity<ZaloUserResponse> getById(String id) {
        Optional<ZaloUser> zaloUser = zaloUserRepository.findById(id);
        return zaloUser.map(user -> ResponseEntity.ok(zaloUserMapper.toResponse(user)))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng Zalo có mã " + id + "!"));
    }

    @Transactional
    public ResponseEntity<ZaloUserResponse> create(ZaloUserRequest request) {
        validateZaloUserRequest(request);
        ZaloUser zaloUser = ZaloUser.builder()
                .id(request.getId())
                .displayName(request.getDisplayName())
                .avatar(request.getAvatar())
                .status(request.getStatus())
                .build();
        zaloUser = zaloUserRepository.save(zaloUser);
        return ResponseEntity.ok(zaloUserMapper.toResponse(zaloUser));
    }

    @Transactional
    public ResponseEntity<ZaloUserResponse> update(String id, ZaloUserRequest request) {
        validateZaloUserRequest(request);
        ZaloUser zaloUser = zaloUserRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng Zalo có mã " + id + "!"));
        zaloUser.setDisplayName(request.getDisplayName());
        zaloUser.setAvatar(request.getAvatar());
        zaloUser.setStatus(request.getStatus());

        zaloUser = zaloUserRepository.save(zaloUser);
        return ResponseEntity.ok(zaloUserMapper.toResponse(zaloUser));
    }

    @Transactional
    public ResponseEntity<String> deleteById(String id) {
        if (!zaloUserRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy người dùng Zalo có mã " + id + "!");
        }
        zaloUserRepository.deleteById(id);
        return ResponseEntity.ok("Người dùng Zalo có mã " + id + " đã bị xóa thành công.");
    }

    public PageResponse<ZaloUserResponse> searchUsers(String keyword, String tagId, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<ZaloUser> zaloUserPage;
        if (tagId == null || tagId.isEmpty())
            zaloUserPage = zaloUserRepository.findByKeyword(keyword, pageable);
        else
            zaloUserPage = zaloUserRepository.findByKeywordAndTag(keyword, tagId, pageable);
        List<ZaloUserResponse> zaloUserResponses = zaloUserPage.getContent().stream()
                .map(zaloUserMapper::toResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(zaloUserPage.getTotalPages(), page, size, zaloUserPage.getTotalElements(), zaloUserResponses);
    }

    public ResponseEntity<String> syncAllZaloUsersToDatabase() {
        try {
            int page = 1;
            int size = 10;
            PageResponse<String> userIdPage = zaloUserUtil.getListFollower(page, size);
            while (page <= userIdPage.getTotalPages()) {
                for (String userId : userIdPage.getData()) {
                    try {
                        ZaloUserProfileResponse userProfile = zaloUserUtil.getUserProfile(userId);
                        ZaloUserRequest request = ZaloUserRequest.builder()
                                .id(userProfile.getUserId())
                                .displayName(userProfile.getDisplayName())
                                .avatar(userProfile.getAvatars().getAvatar240())
                                .status(userProfile.getUserIsFollower())
                                .build();
                        create(request);
                        log.info("Synced user information with id {}", request.getId());
                    } catch (Exception e) {
                        log.error("An error occurred while synchronizing data: {}", e.getMessage());
                    }
                }
                page++;
                userIdPage = zaloUserUtil.getListFollower(page, size);
            }

            return ResponseEntity.ok("Dữ liệu người dùng Zalo đã được đồng bộ thành công!");
        } catch (Exception e) {
            log.error("Lỗi trong quá trình đồng bộ dữ liệu Zalo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đồng bộ dữ liệu thất bại!");
        }
    }
}
