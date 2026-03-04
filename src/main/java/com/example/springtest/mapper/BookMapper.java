package com.example.springtest.mapper;

import com.example.springtest.api.dto.request.BookCreateRequest;
import com.example.springtest.api.dto.response.BookResponse;
import com.example.springtest.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    Book toEntity(BookCreateRequest request);
    BookResponse toResponse(Book book);
    void update(BookCreateRequest request, @MappingTarget Book book);
}
