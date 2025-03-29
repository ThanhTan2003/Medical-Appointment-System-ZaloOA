package com.programmingtechie.zalo_oa_service.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZaloUserProfileResponse {
    private String userId;
    private String userIdByApp;
    private String userExternalId;
    private String displayName;
    private String userAlias;
    private Boolean isSensitive;
    private String userLastInteractionDate;
    private Boolean userIsFollower;
    private String avatar;
    private Avatars avatars;
    private TagsAndNotesInfo tagsAndNotesInfo;
    private SharedInfo sharedInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Avatars {
        private String avatar240;
        private String avatar120;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagsAndNotesInfo {
        private String[] notes;
        private String[] tagNames;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SharedInfo {
        private String address;
        private String city;
        private String district;
        private Long phone;
        private String name;
        private String userDob;
    }
}
