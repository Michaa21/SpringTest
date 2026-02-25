package com.example.springtest.mapper;

import com.example.springtest.api.model.UserCreateRequest;
import com.example.springtest.api.model.UserResponse;
import com.example.springtest.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ProfileMapper.class)
public interface UserApiMapper {
    User toEntity(UserCreateRequest request);

    UserResponse toResponse(User user);

    @AfterMapping
    default void linkProfile(@MappingTarget User user) {
        user.getProfile().setUser(user);
    }
}

