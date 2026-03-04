    package com.example.springtest.mapper;

    import com.example.springtest.api.dto.request.ProfileRequest;
    import com.example.springtest.api.dto.response.ProfileResponse;
    import com.example.springtest.domain.Profile;
    import org.mapstruct.Mapper;
    import org.mapstruct.MappingConstants;
    import org.mapstruct.MappingTarget;

    @Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
    public interface ProfileMapper {
        Profile toEntity(ProfileRequest request);
        ProfileResponse toResponse(Profile profile);
        void update(ProfileRequest request,
                    @MappingTarget Profile profile);
    }
