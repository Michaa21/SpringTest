package com.example.springtest.mapper;

import com.example.springtest.api.dto.request.BookCreateRequest;
import com.example.springtest.api.dto.response.BookResponse;
import com.example.springtest.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    Book toEntity(BookCreateRequest request);


    BookResponse toResponse(Book book);

    @Mapping(target = "author", ignore = true)
    void update(BookCreateRequest request, @MappingTarget Book book);
}
