package com.example.springtest.mapper;

import com.example.springtest.api.model.UserCreateRequest;
import com.example.springtest.api.model.UserResponse;
import com.example.springtest.domain.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserApiMapper {
    User toEntity(UserCreateRequest request);

    UserResponse toResponse(User user);

    @AfterMapping
    default void linkProfile(@MappingTarget User user) {
        if (user.getProfile() != null) {
            user.getProfile().setUser(user);
        }
    }

    void updateFromRequest(UserCreateRequest request, @MappingTarget User user);
}

