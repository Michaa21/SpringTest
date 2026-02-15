package com.example.springtest.mapper;

import com.example.springtest.api.model.ProfileRequest;
import com.example.springtest.api.model.ProfileResponse;
import com.example.springtest.model.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toEntity(ProfileRequest request);
    ProfileResponse toResponse(Profile profile);
}
