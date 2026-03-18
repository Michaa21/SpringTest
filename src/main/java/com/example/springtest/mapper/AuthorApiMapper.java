package com.example.springtest.mapper;

import com.example.springtest.api.dto.request.AuthorCreateRequest;
import com.example.springtest.api.dto.response.AuthorResponse;
import com.example.springtest.domain.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = BookMapper.class)
public interface AuthorApiMapper {
    @Mapping(target = "id", ignore = true)
    Author toEntity(AuthorCreateRequest request);

    AuthorResponse toResponse(Author author);

    @Mapping(target = "id", ignore = true)
    void updateFromRequest(AuthorCreateRequest request, @MappingTarget Author author);
}
