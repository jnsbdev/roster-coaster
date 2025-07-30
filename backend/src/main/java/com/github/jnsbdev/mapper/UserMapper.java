package com.github.jnsbdev.mapper;

import com.github.jnsbdev.dto.user.UserDto;
import com.github.jnsbdev.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.id())
                .name(user.name())
                .email(user.email())
                .organizationIds(user.organizationIds())
                .adminOrganizationIds(user.adminOrganizationIds())
                .build();
    }



}
