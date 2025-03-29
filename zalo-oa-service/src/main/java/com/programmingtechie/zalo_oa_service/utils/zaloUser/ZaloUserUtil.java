package com.programmingtechie.zalo_oa_service.utils.zaloUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.programmingtechie.zalo_oa_service.dto.response.PageResponse;
import com.programmingtechie.zalo_oa_service.dto.response.user.ZaloUserProfileResponse;
import com.programmingtechie.zalo_oa_service.enums.ZaloApiEndpoint;
import com.programmingtechie.zalo_oa_service.oa.ZaloOaClient;
import com.programmingtechie.zalo_oa_service.service.token.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ZaloUserUtil {
    final TokenService tokenService;

    private String getJsonValueOrDefault(JsonObject jsonObject, String key, String defaultValue) {
        return jsonObject != null && jsonObject.has(key) ? jsonObject.get(key).getAsString() : defaultValue;
    }

    private boolean getJsonBooleanOrDefault(JsonObject jsonObject, String key, boolean defaultValue) {
        return jsonObject != null && jsonObject.has(key) ? jsonObject.get(key).getAsBoolean() : defaultValue;
    }

    private Long getJsonLongOrDefault(JsonObject jsonObject, String key, Long defaultValue) {
        return jsonObject != null && jsonObject.has(key) ? jsonObject.get(key).getAsLong() : defaultValue;
    }

    public PageResponse<String> getListFollower(Integer page, Integer size) {
        try {
            ZaloOaClient client = new ZaloOaClient();
            String accessToken = tokenService.getTokenResponse().getAccessToken();

            Map<String, String> headers = new HashMap<>();
            headers.put("access_token", accessToken);

            JsonObject data = new JsonObject();
            data.addProperty("offset", (page - 1) * size);
            data.addProperty("count", size);

            Map<String, Object> params = new HashMap<>();
            params.put("data", data.toString());

            JsonObject response = client.excuteRequest(
                    ZaloApiEndpoint.GET_LIST_FOLLOWER.getUrl(), "GET", params, null, headers, null);

            if (response.has("error") && response.get("error").getAsInt() != 0) {
                throw new RuntimeException(
                        "Lỗi khi gọi API Zalo: " + response.get("message").getAsString());
            }

            JsonObject responseData = response.getAsJsonObject("data");
            int total = responseData.get("total").getAsInt();

            JsonArray usersArray = responseData.getAsJsonArray("users");
            List<String> userIds = new ArrayList<>();

            for (int i = 0; i < usersArray.size(); i++) {
                userIds.add(usersArray.get(i).getAsJsonObject().get("user_id").getAsString());
            }

            return PageResponse.<String>builder()
                    .currentPage(page)
                    .pageSize(size)
                    .totalPages((int) Math.ceil((double) total / size))
                    .totalElements(total)
                    .data(userIds)
                    .build();

        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách follower từ Zalo: {}", e.getMessage());
            throw new IllegalArgumentException("Không thể lấy danh sách follower từ Zalo.");
        }
    }

    public ZaloUserProfileResponse getUserProfile(String userId) {
        try {
            ZaloOaClient client = new ZaloOaClient();
            String accessToken = tokenService.getTokenResponse().getAccessToken();

            Map<String, String> headers = new HashMap<>();
            headers.put("access_token", accessToken);

            JsonObject data = new JsonObject();
            data.addProperty("user_id", userId);

            Map<String, Object> params = new HashMap<>();
            params.put("data", data.toString());

            JsonObject response = client.excuteRequest(
                    ZaloApiEndpoint.GET_FOLLOWER_INFO.getUrl(), "GET", params, null, headers, null);

            if (response.has("error") && response.get("error").getAsInt() != 0) {
                throw new RuntimeException(
                        "Lỗi khi gọi API Zalo: " + response.get("message").getAsString());
            }

            JsonObject dataResponse = response.getAsJsonObject("data");

            return ZaloUserProfileResponse.builder()
                    .userId(getJsonValueOrDefault(dataResponse, "user_id", ""))
                    .userIdByApp(getJsonValueOrDefault(dataResponse, "user_id_by_app", ""))
                    .userExternalId(getJsonValueOrDefault(dataResponse, "user_external_id", ""))
                    .displayName(getJsonValueOrDefault(dataResponse, "display_name", ""))
                    .userAlias(getJsonValueOrDefault(dataResponse, "user_alias", ""))
                    .isSensitive(getJsonBooleanOrDefault(dataResponse, "is_sensitive", false))
                    .userLastInteractionDate(getJsonValueOrDefault(dataResponse, "user_last_interaction_date", ""))
                    .userIsFollower(getJsonBooleanOrDefault(dataResponse, "user_is_follower", false))
                    .avatar(getJsonValueOrDefault(dataResponse, "avatar", ""))
                    .avatars(ZaloUserProfileResponse.Avatars.builder()
                            .avatar240(getJsonValueOrDefault(dataResponse.getAsJsonObject("avatars"), "240", ""))
                            .avatar120(getJsonValueOrDefault(dataResponse.getAsJsonObject("avatars"), "120", ""))
                            .build())
                    .tagsAndNotesInfo(ZaloUserProfileResponse.TagsAndNotesInfo.builder()
                            .notes(
                                    !dataResponse
                                                    .getAsJsonObject("tags_and_notes_info")
                                                    .getAsJsonArray("notes")
                                                    .isEmpty()
                                            ? new String[] {
                                                dataResponse
                                                        .getAsJsonObject("tags_and_notes_info")
                                                        .getAsJsonArray("notes")
                                                        .get(0)
                                                        .getAsString(),
                                                dataResponse
                                                                        .getAsJsonObject("tags_and_notes_info")
                                                                        .getAsJsonArray("notes")
                                                                        .size()
                                                                > 1
                                                        ? dataResponse
                                                                .getAsJsonObject("tags_and_notes_info")
                                                                .getAsJsonArray("notes")
                                                                .get(1)
                                                                .getAsString()
                                                        : ""
                                            }
                                            : new String[] {})
                            .tagNames(
                                    !dataResponse
                                                    .getAsJsonObject("tags_and_notes_info")
                                                    .getAsJsonArray("tag_names")
                                                    .isEmpty()
                                            ? new String[] {
                                                dataResponse
                                                        .getAsJsonObject("tags_and_notes_info")
                                                        .getAsJsonArray("tag_names")
                                                        .get(0)
                                                        .getAsString(),
                                                dataResponse
                                                                        .getAsJsonObject("tags_and_notes_info")
                                                                        .getAsJsonArray("tag_names")
                                                                        .size()
                                                                > 1
                                                        ? dataResponse
                                                                .getAsJsonObject("tags_and_notes_info")
                                                                .getAsJsonArray("tag_names")
                                                                .get(1)
                                                                .getAsString()
                                                        : ""
                                            }
                                            : new String[] {})
                            .build())
                    .sharedInfo(ZaloUserProfileResponse.SharedInfo.builder()
                            .address(getJsonValueOrDefault(dataResponse.getAsJsonObject("shared_info"), "address", ""))
                            .city(getJsonValueOrDefault(dataResponse.getAsJsonObject("shared_info"), "city", ""))
                            .district(
                                    getJsonValueOrDefault(dataResponse.getAsJsonObject("shared_info"), "district", ""))
                            .phone(getJsonLongOrDefault(dataResponse.getAsJsonObject("shared_info"), "phone", null))
                            .name(getJsonValueOrDefault(dataResponse.getAsJsonObject("shared_info"), "name", ""))
                            .userDob(getJsonValueOrDefault(dataResponse.getAsJsonObject("shared_info"), "user_dob", ""))
                            .build())
                    .build();

        } catch (Exception e) {
            log.error("Lỗi khi lấy thông tin người dùng: {}", e.getMessage());
            throw new IllegalArgumentException("Lỗi khi lấy thông tin người dùng!");
        }
    }
}
