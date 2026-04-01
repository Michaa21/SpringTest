package com.example.springtest.mapper;

import com.example.springtest.api.dto.request.AuthorCreateRequest;
import com.example.springtest.api.dto.response.AuthorResponse;
import com.example.springtest.domain.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorApiMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorCreateRequest request);

    AuthorResponse toResponse(Author author);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    void updateFromRequest(AuthorCreateRequest request, @MappingTarget Author author);
}
