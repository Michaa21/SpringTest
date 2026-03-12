package com.example.springtest.mapper;

import com.example.springtest.api.dto.request.UserCreateRequest;
import com.example.springtest.api.dto.response.UserResponse;
import com.example.springtest.domain.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserApiMapper {
    User toEntity(UserCreateRequest request);

    UserResponse toResponse(User user);

    @AfterMapping
    default void linkProfile(@MappingTarget User user) {
        if (user.getProfile() != null) {
            user.getProfile().setUser(user);
        }
    }

    void update(UserCreateRequest request, @MappingTarget User user);
}

