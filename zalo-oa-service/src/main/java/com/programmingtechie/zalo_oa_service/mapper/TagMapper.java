package com.programmingtechie.zalo_oa_service.mapper;

import org.springframework.stereotype.Component;

import com.programmingtechie.zalo_oa_service.dto.response.user.TagResponse;
import com.programmingtechie.zalo_oa_service.entity.Tag;

@Component
public class TagMapper {

    public TagResponse toResponse(Tag tag) {
        return TagResponse.builder().id(tag.getId()).name(tag.getName()).build();
    }
}
