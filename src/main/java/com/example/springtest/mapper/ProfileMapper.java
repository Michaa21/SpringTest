    package com.example.springtest.mapper;

    import com.example.springtest.api.model.ProfileRequest;
    import com.example.springtest.api.model.ProfileResponse;
    import com.example.springtest.domain.Profile;
    import org.mapstruct.Mapper;
    import org.mapstruct.MappingTarget;

    @Mapper(componentModel = "spring")
    public interface ProfileMapper {
        Profile toEntity(ProfileRequest request);
        ProfileResponse toResponse(Profile profile);
        void update(ProfileRequest request,
                    @MappingTarget Profile profile);
    }
